import 'package:flutter/material.dart';
import 'logout.dart';
import 'dart:io';
import 'dart:async';
import 'session.dart';
import 'config.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'chatDetails.dart';
import 'createConversation.dart';
//https://flutterbyexample.com/set-up-app-loading/
//http://cogitas.net/custom-loading-animation-flutter/

class AllChats extends StatefulWidget {
  @override
  AllChatsState createState() {
    return AllChatsState();
  }
}

class AllChatsState extends State<AllChats>{
  bool _loadingInProgress;
  var allChatsData, showChatsData;
  @override
  void initState() {
    super.initState();
    _loadingInProgress = true;
    _loadData();
  }
  Widget get _pageToDisplay {
    if (_loadingInProgress) {
      return _loadingView;
    } else {
      return _homeView;
    }
  }
  void _loadData() async {
    Map<String, String> _heads = {};
    final session = new Session();
    var sessResponse = session.post( url + '/AllConversations', _heads);
    await sessResponse.then((res) {
      Map<String, dynamic> allChatsDataTemp = json.decode(res);
//      print(allChatsDataTemp);
      if (allChatsDataTemp["status"]) {
        allChatsData= allChatsDataTemp["data"];
        showChatsData = allChatsData;
//        print(allChatsData);
      }
      else {
        Navigator.pushReplacementNamed(context, "/LoginForm");
      }
    });

//    await new Future.delayed(new Duration(seconds: 5));
    setState(() {
      _loadingInProgress = false;
    });
  }

//  void iterateMapEntry(key, value) {
////    showChatsData = {};
//    showChatsData[key] = value;
//    print('$key:$value');//string interpolation in action
//  }
  void _filterResult(String term) {

      var temp = [];
//      print(showChatsData);

      for (var chat in allChatsData){
//        print("==========================================");
//        print(chat);
//        print(allChatsData);
        if (chat["uid"].contains(term) || chat["name"].contains(term)){
//          print(chat);
          temp.insert(0, chat);
          print(chat);
        }

      }

      print("==========================================");
      setState(() {
        showChatsData = temp;
      });
  }
  void _chatDetail(var uid, var name) {
//    Navigator.pop(context);
//  uuid = uid;
//  uuname = name;
//    Navigator.pushReplacementNamed(context, "/ChatDetails");
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => ChatDetails(uid:uid, uname:name)),
    );
  }
  Widget get _loadingView {
    return new Center(child: new Text('Loading...'));
  }
//  new TextField(
//              onChanged: (text) {
//                print("First text field: $text");
//              },
//            ),

  Widget get _homeView {
    return new  Container(
      child:  new Column(
        children: <Widget>[
          new TextField(
            onChanged: (text) {
              _filterResult(text);
              },
          ),
          new  Flexible(child: ListView.builder(
            padding: const EdgeInsets.all(16.0),
            itemCount: showChatsData.length * 2,
            itemBuilder: (context, index) {
//              if (index == 0) return new TextField(
//                onChanged: (text) {
//                  _filterResult(text);
//                },
//              );
              if (index.isOdd) return Divider();
              final item = showChatsData[index ~/ 2];
              return ListTile(
                title: new Text(item["name"]),
                subtitle: Text((item["uid"]).toString() + "\n" +
                    (item["last_timestamp"]).toString()),
                onTap: (){ _chatDetail(item["uid"], item["name"]); },
              );
            },
          ) )
        ],
      )
    );

  }




    //    return new Center(child: new Text('ok'));

  @override
  Widget build(BuildContext context) {
//    var container = AppStateContainer.of(context);
//    appState = container.state;
    return new Scaffold(         // Add 6 lines from here...
      appBar: AppBar(
        title: const Text('Chats'),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.home),
            onPressed: () {
//              Navigator.popUntil(context, ModalRoute.withName('/AllChats'));
              Navigator.pushReplacementNamed(context, "/AllChats");
            },
          ),
          IconButton(
            icon: Icon(Icons.create),
            onPressed: () {
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => CreateConversation()),
              );
//              Navigator.of(context).pushReplacement(new MaterialPageRoute(builder: (BuildContext context) =>  new  AllChats());
//              Navigator.pushReplacementNamed(context, "/AllChats");
            },
          ),
          IconButton(
            icon: Icon(Icons.exit_to_app),
            onPressed: () {
              logout();
              Navigator.popUntil(context, ModalRoute.withName('/AllChats'));
              Navigator.pushReplacementNamed(context, "/LoginForm");
//              Navigator.pop(context);
            },
          ),
        ],
      ),
      body: _pageToDisplay,
    );
  }
}