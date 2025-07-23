export class FunctionHelper {
  public static downloadFile(fileName: string, content: any) {
    let dataArray = this.base64ToArrayBuffer(content);
    let blob = new Blob([dataArray], {type: 'octet/stream'});

    if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
      (window.navigator as any).msSaveOrOpenBlob(blob, fileName);
    } else {
      let link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
    }
  }

  public static base64ToArrayBuffer(base64: any): Uint8Array {
    let binaryString = window.atob(base64);
    let binaryLen = binaryString.length;
    let bytes = new Uint8Array(binaryLen);
    for (let i = 0; i < binaryLen; i++) {
      let ascii = binaryString.charCodeAt(i);
      bytes[i] = ascii;
    }
    return bytes;
  }
}
