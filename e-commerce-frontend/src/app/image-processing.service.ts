import { Injectable, Sanitizer } from '@angular/core';
import { Product } from './_model/product.model';
import { FileHandle } from './_model/file-handle.model';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class ImageProcessingService {

  constructor(private sanitizer: DomSanitizer) { }

  //process product images
  public createImages(product: Product) {
    const productImages: any[] = product.productImages;
    const productImagesToFileHandle: FileHandle[] = [];

    for (let i = 0; i < productImages.length; i++) {
      const imageFileData = productImages[i];
      const imageBlob = this.dataURItoBlob(imageFileData.imageByte, imageFileData.type);
      const imageFile = new File([imageBlob], imageFileData.fileName, { type: imageFileData.type });
      const finalFileHandle: FileHandle = {
        file: imageFile,
        url: this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(imageFile))
      };
      productImagesToFileHandle.push(finalFileHandle);
    }
    product.productImages = productImagesToFileHandle;
    return product;
  }

  //convert a data URI to a Blob object
  public dataURItoBlob(picBytes: string, imageType: string): Blob {
    const binary = window.atob(picBytes); // Decode Base64 string
    const arrayBuffer = new ArrayBuffer(binary.length);
    const int8Array = new Uint8Array(arrayBuffer);

    for (let i = 0; i < binary.length; i++) {
      int8Array[i] = binary.charCodeAt(i);
    }
    return new Blob([int8Array], { type: imageType });
  }
}
