import { TestBed } from '@angular/core/testing';

import { CustomDuckService } from './custom-duck.service';

describe('CustomDuckService', () => {
  let service: CustomDuckService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CustomDuckService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
