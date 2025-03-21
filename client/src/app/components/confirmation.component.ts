import { Component, inject, OnInit } from '@angular/core';
import { StoreService } from '../store.service';
import { orderResponse } from '../models';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit{
  
  ngOnInit(): void {
    this.details = this.store.getResponse$

    // this.details.subscribe((x)=> console.info('>>x: ', x))
  }

  // TODO: Task 5

  private store = inject(StoreService)
  private router = inject(Router)

  details !: Observable<orderResponse>

  backToView1(){
    this.router.navigate(['/'])
  }
}
