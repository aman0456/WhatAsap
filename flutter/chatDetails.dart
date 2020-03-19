import 'package:flutter/material.dart';
import 'logout.dart';
import 'dart:io';
import 'dart:async';
import 'session.dart';
import 'config.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'createConversation.dart';
import  'allChats.dart';
class ChatDetails extends StatefulWidget {
  final String uid,  uname;
  ChatDetails({Key key, @required this.uid, @required this.uname}) : super(key: key);
//  ChatDetails({Key key, @required this.name}) : super(key: key);
  @override
  ChatDetailsState createState() {
    return ChatDetailsState();
  }
}

class ChatDetailsState extends State<ChatDetails>{
  bool _loadingInProgress;
  var showChatData;
  final _textController = TextEditingController();
  @override
  void initState() {
    super.initState();
    _loadingInProgress = true;
    _loadData();
  }
  void  _loadData() async {
    Map<String, String> _heads = {"other_id" : widget.uid};
    final session = new Session();
    var sessResponse = session.post( url + '/ConversationDetail', _heads);
    await sessResponse.then((res) {
      Map<String, dynamic> allChatDataTemp = json.decode(res);
//      print(allChatDataTemp);
//      allChatData= ;
      showChatData = allChatDataTemp["data"];
//      print(allChatData);
    });

//    await new Future.delayed(new Duration(seconds: 5));
    setState(() {
      _loadingInProgress = false;
    });
  }
  Widget get _mPageToDisplay {
    if (_loadingInProgress) {
      return _loadingView;
    } else {
      return _homeView;
    }
  }
  Widget get _loadingView {
    return new Center(child: new Text('Loading...'));
  }
  void _sendMsg(String text) async {
    Map<String, String> _heads = {"other_id" : widget.uid, "msg" : text};
    final session = new Session();
    var sessResponse = session.post( url + '/NewMessage', _heads);
    await sessResponse.then((res) {
      Map<String, dynamic> newDataTemp = json.decode(res);
//      _textController.text = "aman";
//        allChatData= allChatDataTemp["data"];
//        showChatData;
//        print(allChatsData
    });

//    await new Future.delayed(new Duration(seconds: 5));
    setState(() {

      _loadData();
      _textController.clear();
    });
  }
  Widget get _homeView {
  return new  Container(
      child:  new Column(
        children: <Widget>[

          new Expanded(child: ListView.builder(
            padding: const EdgeInsets.all(16.0),
            itemCount: showChatData.length,
            itemBuilder: (context, index) {
//              if (index == 0) return new TextField(
//                onChanged: (text) {
//                  _filterResult(text);
//                },
//              );
//              if (index.isOdd) return Divider();
              final item = showChatData[index];
              if (item["uid"] ==  widget.uid){
                return ListTile(
                  title: new Align (child: new Text(item["text"]),
                      alignment: Alignment.centerLeft),
//                  (child: new Text(item["text"])),
                  subtitle: new Align (
                      child: new Text((item["timestamp"]).toString()),
                      alignment: Alignment.centerLeft),
//                onTap: (){ _chatDetail(item["uid"], item["name"]); },
                );
              }
              else {
                return ListTile(
                  title: new Align (child: new Text(item["text"]),
                      alignment: Alignment.centerRight),
//                  (child: new Text(item["text"])),
                  subtitle: new Align (
                      child: new Text((item["timestamp"]).toString()),
                      alignment: Alignment.centerRight),
//                onTap: (){ _chatDetail(item["uid"], item["name"]); },
                );
              }
            },
          ) ),
          new TextFormField(
                  controller: _textController,
                  decoration: InputDecoration(
                      hintText: 'New Message',
                      filled: true,
                      suffixIcon: IconButton(
                          icon: Icon(Icons.send),
                          onPressed: () {
                            _sendMsg(_textController.text);
                          })),
                ),

//              IconButton(
//                icon: Icon(Icons.send),
////                onPressed: () {
////                },
//              ),

        ],
      )
  );
  }
  @override
  Widget build(BuildContext context) {
    return new Scaffold(         // Add 6 lines from here...
      appBar: AppBar(
        title: Text(widget.uname),
        automaticallyImplyLeading: false,
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.home),
            onPressed: () {
              Navigator.popUntil(context, ModalRoute.withName('/AllChats'));
              Navigator.pushReplacementNamed(context, "/AllChats");
//              Navigator.push(
//                context,
//                MaterialPageRoute(builder: (context) => AllChats()),
//              );
              },
          ),
//          IconButton(
//            icon: Icon(Icons.create),
//          o  onPressed: () {
//            },
//          ),
//          IconButton(
//            icon: Icon(Icons.exit_to_app),
//            onPressed: () {
//              logout();
//              Navigator.pop(context);
//            },
//          ),
        ],
      ),
      body: _mPageToDisplay,
    );
  }
}