import 'package:flutter/material.dart';
import 'logout.dart';
import 'dart:io';
import 'dart:async';
import 'session.dart';
import 'config.dart';
import 'dart:convert';
import  'allChats.dart';
import 'chatDetails.dart';
import 'package:http/http.dart' as http;
import 'package:flutter_typeahead/flutter_typeahead.dart';
import 'package:flutter/foundation.dart';
//import 'createConversation.dart';
//https://pub.dartlang.org/packages/flutter_typeahead
class CreateConversation extends StatefulWidget {
  @override
  CreateConversationState createState() {
    return CreateConversationState();
  }
}


//Widget get _mPageToDisplay {
//  return new TextFormField(
//    controller: _textController,
//    decoration: InputDecoration(
//        hintText: 'New Message',
//        filled: true,
//        suffixIcon: IconButton(
//            icon: Icon(Icons.send),
//            onPressed: () {
////              _sendMsg(_textController.text);
//            })),
//  ),
//}

class CreateConversationState extends State<CreateConversation>{

//  final _textController = TextEditingController();
  void  _createConv(String label, String uid) async {
    String name;
    var ind = label.indexOf(", name: ");
    var ind2 = label.indexOf(", phone: ");
    name = label.substring(ind+8, ind2);
    Map<String, String> _heads = {"other_id" : uid};
    final session = new Session();
    var sessResponse = session.post( url + '/CreateConversation', _heads);
    await sessResponse.then((res) {

    });
//    Navigator.pop(context);
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => ChatDetails(uid:uid, uname:name)),
    );
  }
  @override
  Widget build(BuildContext context) {
    return new Scaffold(         // Add 6 lines from here...
      appBar: AppBar(
        title: Text("Create Conversation"),
        actions: <Widget>[
          IconButton(
            icon: Icon(Icons.home),
            onPressed: () {
              Navigator.popUntil(context, ModalRoute.withName('/AllChats'));
              Navigator.pushReplacementNamed(context, "/AllChats");
            },
          ),
//          IconButton(
//            icon: Icon(Icons.exit_to_app),
//            onPressed: () {
//              logout();
//              Navigator.pop(context);
//            },
//          ),
        ],
      ),
      body: TypeAheadField(
        textFieldConfiguration: TextFieldConfiguration(
          autofocus: true,
          decoration: InputDecoration(
              border: OutlineInputBorder(),
              hintText: 'Search'
          ),
        ),
        suggestionsCallback: (pattern) async {
          return await BackendService.getSuggestions(pattern);

        },
        itemBuilder: (context, suggestion) {
          return ListTile(
//            leading: Icon(Icons.shopping_cart),
            title: Text(suggestion['label']),
//            subtitle: Text('\$${suggestion['price']}'),
          );
        },
        onSuggestionSelected: (suggestion) {
          _createConv(suggestion['label'], suggestion['value']);

        },
      ),
    );
  }
}

class BackendService {
  static Future<List> getSuggestions(String query) async {
//    await Future.delayed(Duration(seconds: 1));
//    return List.generate(3, (index) {
//      return {
//        'label': query + index.toString(),
////        'price': Random().nextInt(100)
//      };
//    });
    Map<String, String> _heads = {"term" : query};
    final session = new Session();
    var sessResponse = session.post( url + '/AutoCompleteUser', _heads);
    List<dynamic > searchData;
    await sessResponse.then((res) {
      searchData = json.decode(res);
    });
    return List.generate(searchData.length, (index) {
      return {
        'label' : searchData[index]["label"],
        'value'  : searchData[index]["value"]
      };
    });
  }
}