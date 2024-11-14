import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaFichaUsuarioComponent } from './nueva-ficha-usuario.component';

describe('NuevaFichaUsuarioComponent', () => {
  let component: NuevaFichaUsuarioComponent;
  let fixture: ComponentFixture<NuevaFichaUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NuevaFichaUsuarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NuevaFichaUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
