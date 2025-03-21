import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { item } from '../models';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { StoreService } from '../store.service';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  ngOnInit(): void {
    this.items = this.getMenu();
    // console.info('>>> menu: ', this.items)
  }
  // TODO: Task 2

  private restaurantService = inject(RestaurantService)
  private router = inject(Router)
  private store = inject(StoreService)

  items !: Observable<item[]>
  counter : Map<item, number> = new Map();
  totalPrice = 0
  qty = 0

  getMenu(){
    return this.restaurantService.getMenuItems()
  }

  addItem(x : item){

    if (this.counter.has(x)){
      let count = this.counter.get(x) as number
      this.counter.set(x, count + 1)
    }
    else {
      this.counter.set(x, 1)
    }
    this.totalPrice += x.price
    this.qty += 1

    this.store.addOrder$(x)
  }


  removeItem(x : item){
    this.qty -= 1
    this.totalPrice -= x.price
    let count = this.counter.get(x) as number
    this.counter.set(x, count -1)
    this.store.removeOrder$(x)
  }

  goToView2(){
    // console.info('>>> go to view2')
    this.router.navigate(['place-order'])
  }
}
