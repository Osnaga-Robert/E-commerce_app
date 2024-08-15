import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse, HttpErrorResponse } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { UserAuthService } from "../_services/user-auth.service";
import { catchError } from "rxjs/operators";
import { Router } from "@angular/router";
import { Injectable } from "@angular/core";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private userAuthService: UserAuthService, private router: Router) { }

    //intercepts HTTP requests to add an authorization token
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log("INTERCEPTOR: Request intercepted");

        if (req.headers.get('No-Auth') === "True") {
            return next.handle(req.clone());
        }

        const token = this.userAuthService.getToken();

        console.log("Token:", token);

        if (token) {
            req = this.addToken(req, token);
        }

        return next.handle(req).pipe(
            catchError(
                (error: HttpErrorResponse) => {
                    console.log("Error Status:", error.status);
                    return throwError(() => error);
                }
            )
        );
    }

    //adds the authorization token to the request headers
    private addToken(req: HttpRequest<any>, token: string) {
        return req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        });
    }
}
