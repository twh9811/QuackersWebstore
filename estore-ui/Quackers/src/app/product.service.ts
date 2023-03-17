import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators'

import { Duck } from './duck';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiURL = 'http://localhost:8080/inventory';

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
    const url = `${this.apiURL}`;
    return this.http.get<Duck[]>(url).pipe(tap(_ => console.log("Ducks retrieved")), catchError(this.handleError<Duck[]>('getProducts')));
  }

  /**
   * Retrieves a duck based on its id
   * 
   * @param id The id of the duck being retrieved
   * @returns The duck if found otherwise an empty duck
   */
  getDuck(id: number): Observable<Duck> {
    const url = `${this.apiURL}/product/${id}`;
    return this.http.get<Duck>(url).pipe(tap(_ => console.log(`Duck with Id ${id} retrieved`)), catchError(this.handleError<Duck>('getProduct')));
  }

  /**
   * Deletes a duck with the given id
   * 
   * @param id The id of the duck being deleted
   */
  deleteDuck(id: number): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/product/${id}`;
    return this.http.delete(url, { observe: 'response' }).pipe(tap(_ => console.log(`Duck with Id ${id} deleted`)), catchError(this.handleError<HttpResponse<any>>('deleteDuck')));
  }

  /**
   * Creates a duck
   * 
   * @param duck The duck object that is being created
   * @returns An http response object in which the newly created duck is returned (if there are no error) and the response itself
   */
  createDuck(duck: Duck): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/product`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this.http.post<HttpResponse<any>>(url, duck, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Created duck`)),
        catchError(this.handleError<HttpResponse<any>>('ceateDuck', true)));
  }

  updateDuck(duck: Duck): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/product`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this.http.put<HttpResponse<any>>(url, duck, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Updated duck`)),
        catchError(this.handleError<HttpResponse<any>>('updateDuck', true)));
  }

  /* GET ducks whose name contains search term */
// searchDuck(term: string): Observable<HttpResponse<any>> {
//   const url = `${this.apiURL}/product`;
//   if (!term.trim()) {
//     // if not search term, return empty hero array.
//     return of([]);
//   }
//   return this.http.get<Hero[]>(`${this.heroesUrl}/?name=${term}`).pipe(
//     tap(x => x.length ?
//        this.log(`found heroes matching "${term}"`) :
//        this.log(`no heroes matching "${term}"`)),
//     catchError(this.handleError<Hero[]>('searchHeroes', []))
//   );
// }

  /**
   * Handle http operations that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param shouldReturnError whether the error should be returned and not logged
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', shouldReturnError: boolean = false, result?: T,): any {
    return (error: any): Observable<T> => {
      if (shouldReturnError) return of(error as T);
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      console.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    }
  }

}