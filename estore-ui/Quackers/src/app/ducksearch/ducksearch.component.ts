import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';
import { Observable, Subject } from 'rxjs';

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Duck } from '../duck';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-duck-search',
  templateUrl: './ducksearch.component.html',
  styleUrls: ['./ducksearch.component.css'],
  encapsulation : ViewEncapsulation.None,
})
export class DucksearchComponent implements OnInit {
  @Output() searchEvent = new EventEmitter<Observable<Duck[]>>();

  private searchTerms = new Subject<string>();

  constructor(private productService: ProductService) { }

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    this.searchEvent.emit(this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      // Gets all ducks if term is empty
      switchMap((term: string) => term.length == 0 ? this.productService.getDucks() : this.productService.searchDuck(term))
    ));
  }

}