export interface Account {
    type: string;
    id: number;
    username: string;
    plainPassword: string;
    adminStatus: boolean;
    firstName: string;
    lastName: string;
    address: string;
    city: string;
    zipCode: string;
}