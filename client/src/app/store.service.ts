import { Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { item, orderResponse } from './models';

export interface orderState{
  order : Map<item, number>
  total : number
  response : orderResponse
}

@Injectable({
  providedIn: 'root'
})
export class StoreService extends ComponentStore<orderState>{

  constructor() { super({
    order : new Map(),
    total : 0,
    response : {
      payment_id: '',
      order_id : '',
      total : 0,
      timestamp : 0
    }
    }
    )
  }

  readonly getOrder$ = this.select(x => x.order)

  readonly getTotal$ = this.select(x => x.total)

  readonly getResponse$ = this.select(x => x.response)

  readonly addOrder$ = this.updater<item>(
    (o : orderState, i : item) => {
      let counter = o.order
      if (!counter.has(i)){
        counter.set(i, 1)
      }
      else{
        let n = counter.get(i) as number
        counter.set(i, n+1)
      }

      const newState : orderState = {
        order : counter,
        total : o.total + i.price,
        response : o.response
      }
      return newState
    }
  )

  readonly removeOrder$ = this.updater<item>(
    (o : orderState, i : item) => {
      let counter = o.order
      if (counter.has(i)){
        let n = counter.get(i) as number
        if (n>1){
          counter.set(i, n-1)
        }
        else {
          counter.set(i, 0)
        }
        
      }
      const newState : orderState = {
        order : counter,
        total : o.total - i.price,
        response : o.response
      }
      return newState
    }
  )

  readonly setResponse$ = this.updater<orderResponse>( (o: orderState, r: orderResponse) => {
    return {
      ...o,
      response : r
    } as orderState
  }

  )

  readonly clearOrder = this.updater(() => ({
      order : new Map(),
      total : 0,
      response : {
        order_id : '',
        payment_id : '',
        timestamp : 0,
        total : 0
      }
    })
  );
}
