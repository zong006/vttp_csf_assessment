import { Component, inject, OnInit } from '@angular/core';
import { StoreService } from '../store.service';
import { item, Order, orderResponse, sendItem } from '../models';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{
  ngOnInit(): void {
    this.order = this.store.getOrder$
    this.total = this.store.getTotal$

    this.form = this.createForm()
  }

  // TODO: Task 3

  foodOrder : Order = {
    username : '',
    password: '',
    items : []
  }

  private store = inject(StoreService)
  private router = inject(Router)
  private restaurantService = inject(RestaurantService)

  order !: Observable<Map<item, number>> 
  total !: Observable<number>
  
  fb = inject(FormBuilder)
  form !: FormGroup

  startOver(){
    this.store.clearOrder()
    this.router.navigate(['/'])
  }

  createForm():FormGroup{
    return this.fb.group({
      username : this.fb.control<string>('', [Validators.required]),
      password : this.fb.control<string>('', [Validators.required]),
    })
  }

  processForm(){
    const entry = this.form.value
    this.foodOrder.username = entry.username
    this.foodOrder.password = entry.password

    this.order.subscribe(
      (data) => {
        data.forEach((v : number, k : item) => {

          let si : sendItem = {
            id : '',    
            price : 0,
            quantity : 0
          }

          si.id = k.id
          si.price = k.price
          si.quantity = v

          this.foodOrder.items.push(si)
        }) 
      }
    )

    // console.info('>>> form: ', this.foodOrder)

    this.restaurantService.sendFoodOrder(this.foodOrder).subscribe(
      {
        next: (data) => {
          // console.info('>>> response: ', data)
          // console.info('>>> status: ', data.status)
          this.store.setResponse$( data.body as orderResponse)
          this.router.navigate(['/confirmation'])
          
        },
        error: (e) => {
          alert('ERROR: '+ e.error.message);
        }
      }
    )
  }
}
