import { Category } from "./category.model";
import { FileHandle } from "./file-handle.model";

export interface Product {
    productId: number;
    productName: string;
    productDescription: string;
    productFromDiscounted: string,
    productToDiscounted: string,
    productDiscounted: number;
    productPrice: number;
    productQuantity: number;
    productCompanySeller: string;
    productImages: FileHandle[];
    productCategory: Category[];
}