import { TestBed } from '@angular/core/testing';

import { FichaUsuarioService } from './ficha-usuario.service';

describe('FichaUsuarioService', () => {
  let service: FichaUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FichaUsuarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
