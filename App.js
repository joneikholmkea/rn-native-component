import React from 'react';
import { StyleSheet,View, Button, Image } from 'react-native';
import CustomImagePickerModule from './CustomImagePickerModule'; // Adjust the path as needed

export default function App() {
  const [image, setImage] = React.useState(null);

  const handlePickImage = async () => {
    try {
      const startTime = Date.now();
      const imageBase64 = await CustomImagePickerModule.openPicker();
      //const uri = await CustomImagePickerModule.openPicker();
      console.log('Time to receive image URI: ', Date.now() - startTime, ' ms');
      const startTime2 = Date.now();
      setImage(`data:image/jpeg;base64,${imageBase64}`);
      //setImage(uri)
      console.log('Time to set image state: ', Date.now() - startTime2, ' ms');

    } catch (error) {
      console.log('Error', error.message);
    }
  };

  return (
    <View style={styles.container}>
      <Button title="Pick an Image" onPress={handlePickImage} />
      {image && <Image source={{ uri: image }} style={{ width: 200, height: 200 }} />}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
