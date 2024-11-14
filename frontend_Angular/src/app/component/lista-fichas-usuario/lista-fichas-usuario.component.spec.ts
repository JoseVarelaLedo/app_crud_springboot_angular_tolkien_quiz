import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaFichasUsuarioComponent } from './lista-fichas-usuario.component';

describe('ListaFichasUsuarioComponent', () => {
  let component: ListaFichasUsuarioComponent;
  let fixture: ComponentFixture<ListaFichasUsuarioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaFichasUsuarioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListaFichasUsuarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
