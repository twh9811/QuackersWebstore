import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators'

import { Duck } from './duck';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiURL = 'http://localhost:8080';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  /**
   * Gets all of the ducks currently in the inventory from the ducks-api
   * 
   * @returns An array of Ducks containing all of the ducks in the inventory
   */
  getDucks(): Observable<Duck[]> {
    const url = `${this.apiURL}/inventory`;
    return this.http.get<Duck[]>(url).pipe(tap(_ => console.log("Ducks retrieved")), catchError(this.handleError<Duck[]>('getProducts')));
  }

  /**
   * Retrieves a duck based on its id
   * 
   * @param id The id of the duck being retrieved
   * @returns The duck if found otherwise an empty duck
   */
  getDuck(id: number): Observable<Duck> {
    const url = `${this.apiURL}/inventory/${id}`;
    return this.http.get<Duck>(url).pipe(tap(_ => console.log(`Duck with Id ${id} retrieved`)), catchError(this.handleError<Duck>('getProduct')));
  }

  /**
   * Deletes a duck with the given id
   * 
   * @param id The id of the duck being deleted
   */
  deleteDuck(id: number): Observable<Duck | HttpResponse<Object>> {
    const url = `${this.apiURL}/inventory/product/${id}`;
    return this.http.delete(url, { observe: 'response' }).pipe(tap(_ => console.log(`Duck with Id ${id} deleted`)), catchError(this.handleError<Duck>('deleteDuck')));
  }

  /**
   * Handle http operations that failed.
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
    }
  }
}
