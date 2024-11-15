import { Component, HostListener, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { NgIf, NgFor } from '@angular/common';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, FormsModule, TranslateModule, NgIf, NgFor],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'BD Management Tolkien Quiz';

  activeLangIcon = 'assets/ico/es.png';   // icono e idioma activo por defecto
  activeLangLabel = 'Español';

  languages = [                                                     // lista de idiomas disponibles
    { code: 'es', label: 'Español', icon: 'assets/ico/es.png' },
    { code: 'en', label: 'English', icon: 'assets/ico/en.png' },
    { code: 'gz', label: 'Galego', icon: 'assets/ico/gz.png' },
    { code: 'ek', label: 'Euskera', icon: 'assets/ico/ek.png' },
    { code: 'ct', label: 'Catalá', icon: 'assets/ico/ct.png' }
  ];

  rol: string | null= null;
  username: string | null = null;
  langMenuVisible = false;
  isSidebarVisible = true;
  isLoggedIn: boolean = false;
  isLoginPage: boolean;
  zoomLevel = 1;


  constructor(private readonly authService: AuthService, private readonly translate: TranslateService, private readonly router:Router) {

    this.translate.addLangs(['en', 'es', 'gz', 'ek', 'ct']);                         // definimos los idiomas disponibles
    this.translate.setDefaultLang('es');                                             // idioma predeterminado

    const browserLang = this.translate.getBrowserLang();                             // elegir un idioma según la preferencia del navegador o usar el idioma predeterminado
    this.translate.use(browserLang?.match(/en|es|gz|ek|ct/) ? browserLang : 'es');
    this.router.events.subscribe(() => {
      this.isSidebarVisible = this.router.url !== '/login';                         // ocultar la barra lateral si la ruta es "login"
    });
    this.isLoginPage = this.router.url === '/login';
    this.router.events.subscribe(() => {
      this.isLoginPage = this.router.url === '/login';
    });
  }

  ngOnInit(): void {
    this.authService.loggedIn$.subscribe(isLoggedIn => {
      this.rol = localStorage.getItem('role');
      this.username = localStorage.getItem('username');
      this.isLoggedIn = isLoggedIn;

      this.setZoomLevel(window.innerWidth);       //para ajuste del nivel de zoom en función del tamaño de pantalla, usando los dos métodos siguientes
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    const windowWidth = (event.target as Window).innerWidth;
    this.setZoomLevel(windowWidth);
  }

  setZoomLevel(width: number) {
    if (width < 1200) {
      this.zoomLevel = 0.7;
    } else if (width < 768) {
      this.zoomLevel = 0.65;
    } else {
      this.zoomLevel = 1;
    }
  }

  changeLanguage(lang: string) {            // cambio del idioma de la aplicación
    this.translate.use(lang);
    const selectedLang = this.languages.find(language => language.code === lang);
    if (selectedLang) {
      this.activeLangIcon = selectedLang.icon;
      this.activeLangLabel = selectedLang.label;
    }
    this.langMenuVisible = false; // así logramos que el menú después de seleccionar un idioma se oculte
  }

  toggleLanguageMenu(visible: boolean) {      // alternar el botón de menú de idioma
    this.langMenuVisible = visible;
  }

  logout(){
    this.authService.logout();
  }
}
