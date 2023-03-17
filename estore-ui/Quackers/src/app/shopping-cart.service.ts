import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';

import { Observable,of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators'

import { Cart } from './shopping-cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private http: HttpClient) { }

  getCart(id : number) : Observable<Cart>{
    const url = 'http://localhost:8080/cart/1'
    //const url = `${this.apiURL}/cart/${id}`;
    return this.http.get<Cart>(url).pipe(
      tap(_ => console.log(`got cart ${id}`)), catchError(this.handleError<any>('get cart'))
    );
  }

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
