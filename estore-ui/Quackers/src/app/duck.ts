/**
 * Interface for the Duck Object
 */
export interface Duck {
    id: number,
    name: string,
    quantity: number,
    price: string,
    size: string,
    color: string,
    outfit: DuckOutfit
}

/**
 * Interface for the DuckOutfit object
 */
export interface DuckOutfit {
    hatUID: number,
    shirtUID: number,
    shoesUID: number,
    handItemUID: number,
    jewelryUID: number,
}