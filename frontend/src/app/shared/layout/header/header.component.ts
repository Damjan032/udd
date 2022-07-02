import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/core/services/authentication.service';
import { JwtService } from 'src/app/core/services/jwt.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  loggedIn = false;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
    private dialog: MatDialog,
    private jwtService: JwtService
  ) { }

  ngOnInit(): void {
    let token = this.jwtService.getToken();
    if (token) {
      this.loggedIn = true;
    }
  }

  logout(): void {
    this.authenticationService.logout();
    this.loggedIn = false;
    this.router.navigate(['/']);
  }

}
