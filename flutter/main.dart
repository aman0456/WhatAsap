import 'package:flutter/material.dart';
import 'login.dart';
import 'logout.dart';
import 'allChats.dart';
import 'chatDetails.dart';
import 'createConversation.dart';
//https://flutter.io/cookbook/forms/validation/
//https://iirokrankka.com/2017/10/17/validating-forms-in-flutter/
void main()  => runApp(new MyApp());
class MyApp extends  StatelessWidget {
  final  routes = <String, WidgetBuilder>{
    // When we navigate to the "/" route, build the FirstScreen Widget
    "/LoginForm": (context) => LoginForm(),
    // When we navigate to the "/second" route, build the SecondScreen Widget
    "/AllChats": (context) => AllChats(),
    "/ChatDetails": (context) => ChatDetails(),
    "/CreateConversation": (context) => CreateConversation(),
//        "/Logout": (context) => Logout(),
//      '': (context) => SecondScreen(),
  };
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return new MaterialApp(
      title: 'WhatASap',
      debugShowCheckedModeBanner: false,
      home: new LoginForm(),
      routes: routes,
    );
  }
}



//  runApp(MaterialApp(
//    title: 'Named Routes Demo',
//    // Start the app with the "/" named route. In our case, the app will start
//    // on the FirstScreen Widget
//    initialRoute: 'login',
//    routes: <String, WidgetBuilder>{
//      // When we navigate to the "/" route, build the FirstScreen Widget
//      "LoginForm": (context) => LoginForm(),
//      // When we navigate to the "/second" route, build the SecondScreen Widget
//      "AllChats" : (context) => AllChats(),
//      "ChatDetails" : (context) => ChatDetails(),
//      "CreateConversation" : (context) => CreateConversation(),
////      '': (context) => SecondScreen(),
//    },