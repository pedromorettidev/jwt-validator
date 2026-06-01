output "ecr_repository_url" {
  value = aws_ecr_repository.jwt_validator.repository_url
}

output "ecr_repository_arn" {
  value = aws_ecr_repository.jwt_validator.arn
}