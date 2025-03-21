// You may use this file to create any models

export interface item{
    id : string
    name : string
    description : string
    price : number
}

export interface sendItem{
    id : string
    price : number
    quantity : number
}

export interface Order{
    username: string
    password : string
    items : sendItem[]
}

export interface orderResponse {
    order_id : string
    payment_id : string
    total : number
    timestamp : number
}
