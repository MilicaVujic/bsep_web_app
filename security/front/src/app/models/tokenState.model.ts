export interface UserTokenState{
    accesstoken:string;
    refreshtoken:string;
    secretImageUri?:string;
    employee?: boolean;
}