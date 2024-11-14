import { ListaPreguntasComponent } from './component/lista-preguntas/lista-preguntas.component';
import { ListaCategoriasComponent } from './component/lista-categorias/lista-categorias.component';
import { ListaFichasUsuarioComponent } from './component/lista-fichas-usuario/lista-fichas-usuario.component';
import { ListaUsuariosComponent } from './component/lista-usuarios/lista-usuarios.component';
import { NuevoUsuarioComponent } from './component/nuevo-usuario/nuevo-usuario.component';
import { NuevaPreguntaComponent } from './component/nueva-pregunta/nueva-pregunta.component';
import { NuevaFichaUsuarioComponent } from './component/nueva-ficha-usuario/nueva-ficha-usuario.component';
import { NuevaCategoriaComponent } from './component/nueva-categoria/nueva-categoria.component';
import { ActualizarUsuarioComponent } from './component/actualizar-usuario/actualizar-usuario.component';
import { ActualizarPreguntaComponent } from './component/actualizar-pregunta/actualizar-pregunta.component';
import { ActualizarFichaUsuarioComponent } from './component/actualizar-ficha-usuario/actualizar-ficha-usuario.component';
import { ActualizarCategoriaComponent } from './component/actualizar-categoria/actualizar-categoria.component';
import { DetalleUsuarioComponent } from './component/detalles-usuario/detalles-usuario.component';
import { LoginComponent } from './component/login/login.component';
import { HomeComponent } from './home/home.component';

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },                         // redirección a login por defecto
  { path: 'login', component: LoginComponent },                                 // Ruta pantalla de login
  { path: 'home', component: HomeComponent },                                   // Ruta para página de inicio
  { path: 'fichas-usuario', component: ListaFichasUsuarioComponent },           // Ruta para listar fichas de usuario
  { path: 'usuarios', component: ListaUsuariosComponent },                      // Ruta para listar usuarios
  { path: 'categorias', component: ListaCategoriasComponent },                  // Ruta para listar categorias
  { path: 'preguntas', component: ListaPreguntasComponent },                    // Ruta para listar usuarios
  { path: 'nueva-categoria', component: NuevaCategoriaComponent },              // Ruta para registrar nueva categoría
  { path: 'nueva-ficha-usuario', component: NuevaFichaUsuarioComponent },       // Ruta para registrar nuevas ficha de usuario
  { path: 'nueva-pregunta', component: NuevaPreguntaComponent },                // Ruta para registrar nueva pregunta
  { path: 'nuevo-usuario', component: NuevoUsuarioComponent },                  // Ruta para registrar nuevo usuario
  { path: 'actualizar-categoria/id/:id', component: ActualizarCategoriaComponent },         // actualizar categoría
  { path: 'actualizar-ficha-usuario/id/:id', component: ActualizarFichaUsuarioComponent },  // actualizar ficha de usuario
  { path: 'actualizar-pregunta/id/:id', component: ActualizarPreguntaComponent },           // actualizar pregunta
  { path: 'actualizar-usuario/id/:id', component: ActualizarUsuarioComponent },             // actualizar usuario
  { path: 'usuario/:nickname', component: DetalleUsuarioComponent },                        // página donde ver usuario individualizado

  { path: '**', redirectTo: '' }                                                // Redirección para rutas no encontradas (404)
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
