import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { firstValueFrom, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators'
import { Cart } from './shopping-cart';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiURL = 'http://localhost:8080/cart';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, private notificationService: NotificationService) { }

  /**
   * Gets a shopping cart with the given id
   * 
   * @param id The id of the cart
   * @returns An observable containing the cart if found
   */
  getCart(id: number): Observable<Cart> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Cart>(url).pipe(
      tap(_ => console.log(`got cart ${id}`)), catchError(this.handleError<any>('get cart'))
    );
  }

  /**
   * Gets a cart for the user with the given id. If the cart doesn't exist,
   * it will attempt to create one
   * 
   * @param id The id of the user
   * @returns A promise containing the cart or undefined if unable to create one
   */
  async getCartAndCreate(id: number): Promise<Cart | undefined> {
    let cart = await firstValueFrom(this.getCart(id));
    if (cart) return cart;

    const newCart = <Cart>{
      customerId: id,
      items: new Object()
    };

    cart = await firstValueFrom(this.createCart(newCart));
    if (cart) return cart;

    this.notificationService.add(`Unable to create a cart for the user with an id of ${id}`, 3);
    return undefined;
  }

  /**
   * Creates a cart
   * 
   * @param cart The Cart object that is being created
   * @returns An http response object in which the newly created cart is returned (if there are no error) and the response itself
   */
  createCart(cart: Cart): Observable<Cart> {
    return this.http.post<Cart>(this.apiURL, cart, this.httpOptions).pipe(
      tap((newCart: Cart) => console.log(`added cart w/ id=${newCart.customerId}`)),
      catchError(this.handleError<Cart>('addCart'))
    );
  }

  /**
   * Adds a given quantity of a given item to a given cart
   * 
   * @param cart The shopping cart being modifed
   * @param itemId The item id being modified
   * @param quantity The quantity of the item being added
   * @returns An HTTP response containing the status and updated cart object
   */
  addItem(cart: Cart, itemId: number, quantity: number): Observable<HttpResponse<any>> {
    let cartQuantity = cart.items[itemId] ? cart.items[itemId] + quantity : quantity;
    cart.items[itemId] = cartQuantity;
    return this.updateCart(cart);
  }

  /**
   * Updates a given cart
   * 
   * @param cart The cart being updated
   * @returns An HTTP response containing the status and updated cart object
   */
  updateCart(cart: Cart): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/`;

    // No idea why it won't let me store the httpOptions in an object and pass them as a parameter. So I have to do what I do below
    return this.http.put<HttpResponse<any>>(url, cart, { observe: 'response', headers: new HttpHeaders({ 'Content-Type': 'application/json' }) })
      .pipe(tap(_ => console.log(`Updated cart`)),
        catchError(this.handleError<HttpResponse<any>>('updateCart')));
  }

  /**
   * Validates a cart
   * 
   * @param id The id of the cart
   * @returns Either just a response code or a newly validated cart
   */
  validateCart(id: number): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/checkout/validate/${id}`;
    return this.http.get<HttpResponse<any>>(url, { observe: 'response' })
      .pipe(tap(_ => console.log(`Validated cart`)),
        catchError(this.handleError<HttpResponse<any>>('validateCart')));
  }

  /**
   * Checksout a cart
   * 
   * @param id The id of the cart
   * @returns A response code with the updated cart (if successful)
   */
  checkoutCart(id: number): Observable<HttpResponse<any>> {
    const url = `${this.apiURL}/checkout/checkout/${id}`;
    return this.http.put<HttpResponse<any>>(url, { observe: 'response' })
      .pipe(tap(_ => console.log(`Checked out cart`)),
        catchError(this.handleError<HttpResponse<any>>('checkoutCart')));
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
