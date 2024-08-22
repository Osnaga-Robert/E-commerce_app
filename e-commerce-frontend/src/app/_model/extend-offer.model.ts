import { Product } from "./product.model";

export interface ExtendOffer {
    extendOfferProduct: Product | null;
    extendOfferFromDate: string;
    extendOfferToDate: string;
    extendErrorMessage: string;
  }
  