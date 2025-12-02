import { IsEnum } from "class-validator";
import { Role } from "../entities/user.entity";

export class UpdateRoleDto {
    @IsEnum(Role)
    role: Role;
}