import { Product } from "./product.model";

export interface MyOrderDetails{
    orderId: number;
    orderFullName: string;
    orderFullAddress: string;
    orderContactNumber: string;
    orderStatus: string;
    orderAmount: number;
    orderProduct: Product;
    user: any;
}