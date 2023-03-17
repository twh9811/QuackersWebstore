import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';

import { Observable,of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators'

import { Account } from './account';
import { Cart } from './shopping-cart'

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiURL = 'http://localhost:8080';
  
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { }


  getAccount(id: number) : Observable<Account> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Account>(url).pipe(
      tap(_ => console.log(`got account ${id}`)), catchError(this.handleError<any>('get account'))
    );
  }

  getCart() : Observable<Cart> {
    const account = this.getAccount;
    const url = `${this.apiURL}/${account.id}`;
    return this.http.get<Cart>(url).pipe(tap(__ => console.log(`got cart ${account.id}`)), catchError(this.handleError<any>('get cart')))
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
