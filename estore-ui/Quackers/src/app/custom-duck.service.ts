import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, catchError, firstValueFrom, forkJoin, of, tap } from 'rxjs';
import { Duck } from './duck';
import { Account } from './account';

@Injectable({
  providedIn: 'root'
})
export class CustomDuckService {
  newDuck = new Subject<Duck>();

  private apiURL = 'http://localhost:8080/customduck';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private _http: HttpClient) { }


  /**
   * Sends a new duck update to all class listening to newDuck
   * 
   * @param account The account that created the duck
   * @param duck The new duck
   */
  sendNewDuck(account: Account, duck: Duck) {
    this.removeDuckPrefix(account, duck);
    this.newDuck.next(duck);
  }

  /**
   * Gets all of the ducks currently in the inventory from the ducks-api
   * 
   * @returns An array of Ducks containing all of the ducks in the inventory
   */
  private getDucks(): Observable<Duck[]> {
    const url = `${this.apiURL}`;
    return this._http.get<Duck[]>(url).pipe(
      tap(_ => console.log("Custom Ducks retrieved")),
      catchError(this.handleError<Duck[]>('get custom ducks')));
  }

  /**
   * Retrieves a duck based on its id
   * 
   * @param id The id of the duck being retrieved
   * @returns The duck if found otherwise an empty duck
   */
  private getDuck(id: number): Observable<Duck> {
    const url = `${this.apiURL}/${id}`;
    return this._http.get<Duck>(url).pipe(
      tap(_ => console.log(`Custom Duck with Id ${id} retrieved`)),
      catchError(this.handleError<Duck>('get custom duck')));
  }

  /**
   * Deletes a duck with the given id
   * 
   * @param id The id of the duck being deleted
   */
  deleteDuck(id: number): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/${id}`;
    return this._http.delete(url, { observe: 'response' }).pipe(tap(_ => console.log(`Custom Duck with Id ${id} deleted`)), catchError(this.handleError<HttpResponse<any>>('deleteDuck')));
  }

  /**
   * Creates a duck
   * 
   * @param duck The duck object that is being created
   * @returns An http response object in which the newly created duck is returned (if there are no error) and the response itself
   */
  private createDuck(duck: Duck): Observable<HttpResponse<Duck>> {
    const url = `${this.apiURL}/`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this._http.post<HttpResponse<any>>(url, duck, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Created custom duck`)),
        catchError(this.handleError<HttpResponse<any>>('createCutomDuck', true)));
  }

  private updateDuck(duck: Duck): Observable<HttpResponse<Duck>> {
    const url = `${this.apiURL}/`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this._http.put<HttpResponse<any>>(url, duck, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Updated custom duck`)),
        catchError(this.handleError<HttpResponse<any>>('updateCustomDuck', true)));
  }

  /**
   * Gets all custom ducks associated with an account
   * 
   * @param account The account the ducks are being retrieved for
   * @returns All found custom ducks
   */
  async getDucksForAccount(account: Account): Promise<Duck[]> {
    let ducks = await firstValueFrom(this.getDucks());
    // /u200B is a 0 width space
    ducks = ducks.filter(duck => duck.name.startsWith(`${account.username} \u200B- `));

    ducks.forEach(duck => {
      this.removeDuckPrefix(account, duck);
    })
    return ducks;
  }

  /**
   * Removes the name prefix of custom ducks
   * 
   * @param account The account the duck was created by
   * @param duck The duck object
   */
  private removeDuckPrefix(account: Account, duck: Duck): void {
    duck.name = duck.name.replace(`${account.username} \u200B- `, "");
  }

  /**
   * Creates a duck for an account (name is appended with "<username> <0 width space>-")
   * 
   * @param account The account the duck is being created for
   * @param duck The duck
   * @returns An http response
   */
  createDuckForAccount(account: Account, duck: Duck): Observable<HttpResponse<Duck>> {
    const currentName = duck.name;
    duck.name = `${account.username} \u200B- ${currentName}`;

    return this.createDuck(duck);
  }

  /**
   * Updates a duck for an account
   * 
   * @param account The account the duck is being updated for
   * @param duck The new duck object
   * @returns An http response
   */
  updateDuckForAccount(account: Account, duck: Duck): Observable<HttpResponse<Duck>> {
    // Clones the duck so there is no name flashing when the name is updated
    let clone: Duck = { ...duck };

    const currentName = duck.name;
    clone.name = `${account.username} \u200B- ${currentName}`;

    return this.updateDuck(clone);
  }

  async deleteAllDucksForAccount(account: Account): Promise<HttpResponse<Duck>[] | null> {
    let ducks = await this.getDucksForAccount(account);
    if (ducks.length == 0) return null;
    return firstValueFrom(forkJoin(ducks.map(duck => this.deleteDuck(duck.id))));
  }

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
