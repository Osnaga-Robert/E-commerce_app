import { Product } from "./product.model";
import { User } from "./user.model";

export interface MyOrderDetails{
    orderId: number;
    orderFullName: string;
    orderFullAddress: string;
    orderContactNumber: string;
    orderStatus: string;
    orderAmount: number;
    orderPrice: number;
    orderQuantity: number;
    orderDate: string;
    orderProduct: Product;
    user: User;
}