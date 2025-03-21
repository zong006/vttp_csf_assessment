import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MenuComponent } from './components/menu.component';
import { ConfirmationComponent } from './components/confirmation.component';
import { PlaceOrderComponent } from './components/place-order.component';

const routes: Routes = [
  {path:'', component:MenuComponent},
  {path:'place-order', component:PlaceOrderComponent},
  {path:'confirmation', component: ConfirmationComponent}
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
