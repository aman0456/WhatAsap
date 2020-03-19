import 'session.dart';
import 'config.dart';

void logout(){
  Map<String, String> _heads = {};
  final session = new Session();
  var sessResponse = session.post( url + '/LogoutServlet', _heads);

}