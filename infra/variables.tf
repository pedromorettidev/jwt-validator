variable "aws_region" {
  default = "us-east-1"
}

variable "ecr_repository_name" {
  default = "jwt-validator"
}

variable "subnets" {
  type = list(string)
}

variable "security_groups" {
  type = list(string)
}