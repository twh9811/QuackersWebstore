import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';

import {
   debounceTime, distinctUntilChanged, switchMap
 } from 'rxjs/operators';

import { Duck } from '../duck';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-duck-search',
  templateUrl: './ducksearch.component.html',
  styleUrls: [ './ducksearch.component.css' ]
})
export class DucksearchComponent implements OnInit {
  ducks$!: Observable<Duck[]>;
  private searchTerms = new Subject<string>();

  constructor(private productService: ProductService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.ducks$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.productService.searchDuck(term)),
    );
  }
}