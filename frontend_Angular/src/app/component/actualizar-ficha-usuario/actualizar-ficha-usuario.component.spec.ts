import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActualizarFichaUsuarioComponent } from './actualizar-ficha-usuario.component';

describe('ActualizarFichaUsuarioComponent', () => {
  let component: ActualizarFichaUsuarioComponent;
  let fixture: ComponentFixture<ActualizarFichaUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActualizarFichaUsuarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActualizarFichaUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
