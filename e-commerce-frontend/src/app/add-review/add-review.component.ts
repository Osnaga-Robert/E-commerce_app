import { Component, OnInit } from '@angular/core';
import { Review } from '../_model/review.model';
import { ProductService } from '../_services/product.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-review',
  templateUrl: './add-review.component.html',
  styleUrls: ['./add-review.component.css']
})
export class AddReviewComponent implements OnInit{

  reviewText: string = '';
  rating: number | null = null;
  ratings: number[] = [1, 2, 3, 4, 5];
  productId: any;
  errorMessage: string = "";

  constructor(private productService: ProductService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.errorMessage = "";
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('productId');
    });
  }

  submitReview() {
    if (this.reviewText && this.rating) {
      
      const review: Review = {
        rating: this.rating,
        comment: this.reviewText,
        buyerEmail: "",
        buyerName: ""
      };

      console.log("Submitting review for productId -> " + this.productId);

      this.productService.addReview(this.productId, review).subscribe({
        next: (next: any) => {
          console.log("Review added");
          console.log(next);
          this.errorMessage = "";
        },
        error: (error : any) => {
          console.log(error);
          this.errorMessage = error.error.message;
        }
      });

      this.reviewText = '';
      this.rating = null;
    }
  }
}
