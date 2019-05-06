import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

class HelloWorldApp extends React.Component {
  render() {
return (
  <View style={styles.container}>
<Text style={styles.hello}>Hello world! I am from ReactNative!!</Text>
  </View>
)
  }
}
var styles = StyleSheet.create({
  container: {
flex: 1,
justifyContent: 'center',
  },
  hello: {
fontSize: 20,
textAlign: 'center',
margin: 10,
  },
});
//这里的第一个参熟名字要和我们创建的这个工程项目名一样
AppRegistry.registerComponent('damo', () => HelloWorldApp);