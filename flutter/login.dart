import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:async';
import 'dart:convert';
import 'session.dart';
import 'config.dart';
import 'allChats.dart';

// Create a Form Widget
class LoginForm extends StatefulWidget {
  @override
  LoginFormState createState() {
    return LoginFormState();
  }
}

class LoginFormState extends State<LoginForm> {
  final _formKey = GlobalKey<FormState>();
  final appTitle = 'WhatAsap Login';
  String uname;
  String password;
  void _showChats() {
    Navigator.pushReplacementNamed(context, "/AllChats");
  }
  void _performLogin(var context) {
    Map<String, String> _heads = {'userid': uname, 'password': password};
    print(uname);
    print(password);
    final session = new Session();
    var sessResponse = session.post( url + '/LoginServlet', _heads);
    sessResponse.then((res) {
      Map<String, dynamic> sessR = json.decode(res);
      print(sessR);
      if (sessR["status"]) {
        _showChats();
      }
      else  {
        print("hello");
        //make snackbar
        Scaffold.of(context)
            .showSnackBar(SnackBar(content: Text('Invalid Credentials')));

      }
    });
    //print(json.decode(x));
//    final snackbar = SnackBar(
//      content: Text('Email: $_email, password: $_password'),
//    );

//    scaffoldKey.currentState.showSnackBar(snackbar);
  }
  @override
  Widget build(BuildContext context) {
    // Build a Form widget using the _formKey we created above
    return Scaffold(
      appBar: AppBar(
        title: Text(appTitle),
      ),
      body: Builder(
      builder: (context) =>
        new Form(
        key: _formKey,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Text("Username:"),
            TextFormField(
              validator: (value) {
                if (value.isEmpty) {
                  return 'User name required';
                }
              },
              onSaved: (val) => uname = val,
            ),
            Text("Password:"),
            TextFormField(
              obscureText: true,
              onSaved: (val) => password = val,
            ),

            Padding(
              padding: const EdgeInsets.symmetric(vertical: 16.0),
              child: RaisedButton(
                onPressed: () {
                  if (_formKey.currentState.validate()) {
                    _formKey.currentState.save();
                    _performLogin(context);
                  }
                },
                child: Text('Submit'),
              ),
            ),
          ],
        ),
      ),
      ),

    );
  }
}
