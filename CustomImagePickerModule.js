import { NativeModules } from 'react-native';

const { CustomImagePickerModule } = NativeModules;

const openPicker = () => {
  return CustomImagePickerModule.openPicker();
};

export default {
  openPicker,
};