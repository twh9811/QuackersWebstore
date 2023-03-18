import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';

import { Observable,of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators'

import { Cart } from './shopping-cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiURL = 'http://localhost:8080/cart';

httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getCart(id : number) : Observable<Cart>{
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Cart>(url).pipe(
      tap(_ => console.log(`got cart ${id}`)), catchError(this.handleError<any>('get cart'))
    );
  }

  /**
   * Creates a cart
   * 
   * @param cart The Cart object that is being created
   * @returns An http response object in which the newly created cart is returned (if there are no error) and the response itself
   */
  createCart(cart : Cart) : Observable<Cart>{
    return this.http.post<Cart>(this.apiURL, cart, this.httpOptions).pipe(
      tap((newCart: Cart) => console.log(`added cart w/ id=${newCart.customerId}`)),
      catchError(this.handleError<Cart>('addCart'))
    );
  }

  updateCart(cart : Cart): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/cart`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this.http.put<HttpResponse<any>>(url, cart, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Updated cart`)),
        catchError(this.handleError<HttpResponse<any>>('updateCart')));
  }

  deleteCart(id : number) : Observable<Cart> {
    const url = `${this.apiURL}/${id}`;

    return this.http.delete<Cart>(url, this.httpOptions).pipe(
      tap(_ => console.log(`deleted hero id=${id}`)),
      catchError(this.handleError<Cart>('deleteHero'))
    );


  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
