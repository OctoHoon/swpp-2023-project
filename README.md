
# Our app : Eat and Tell (EaT)

<img src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/106653382/d84fddd3-65a6-4ddb-99ab-f0d6f9452691" width="150" height="150"/>


EatAndTell is a restaurant review-dedicated SNS, motivated by the lack of personalization in existing restaurant review services. Just simply write restaurant reviews! Based on your reviews, our app creates tags for you to capture your taste. 
With EatAndTell, enjoy a best-fit SNS tailored to your personal tastes and enhance your dining quality!


# System Design

## System Architecture

We will develop a 3-tier application comprising the presentation, application, and data tiers. 

In the presentation tier, an Android application will be crafted using Kotlin and Jetpack Compose. 

The backend, representing the application tier, will be hosted on Amazon EC2. The typical User API will mainly utilize Django, and machine learning classification will be powered by the Huggingface library and Pytorch. We will also utilize Kakao API as an external API for searching restaurants and crawling data for tag classification of posts.

For the data tier, we will use RDS with MySQL. Since users will upload images, AWS S3 will also be integrated.

![image](https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144795013/d2762abf-d709-49e5-8e94-2aee93d9bd43)

## Class Diagrams and Data Models

### Data Model

![data_model](https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/19504579/e0fc37c4-8987-4173-970d-58f429d37b12)

The following diagram illustrates an Entity Relationship Diagram (ERD) outlining the Django models for our service. Each box corresponds to an individual model and delineates its associated fields. The arrows signify the relationships between the models, capturing the intricate connections within our system.

This database schema represents a system for managing user-generated content related to restaurant experiences. Users can create posts about restaurants, with each post linked to a specific user and restaurant. Users can also establish relationships by following or being followed by other users. Additionally, users can get user tags identified through preference analysis AI, and posts can include multiple photos. The schema also includes a mechanism for users to like posts, creating a many-to-many relationship between users and posts.


### Frontend Class Diagram

![FrontEnd Class Diagram](https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/19504579/b1e98234-607d-4ef3-adbc-fcabc19b3abe)

The frontend class diagram consists of mainly three kind of classes. 
- Activities: There are two activities in our app, which are `StartActivity` and `AppMainActivity`. `StartActivity` is the first screen that users see when they open the app. It is responsible for showing the splash screen and redirecting to the login screen. `AppMainActivity` is the main screen of the app. It is responsible for showing the bottom navigation bar and switching between different screens.
- View Models: There are two view models in our app, which are `AppMainViewModel` and `StartViewModel`, each corresponding to the activity of the same name. These view models are responsible for handling the business logic of the corresponding activity.
- Repository: There is one single `ApiRepository` class in our app. This class is responsible for handling all the API calls to the backend server.
- Composables: There are various composables responsible for rendering different screens,and are marked with `@Composable` in our class diagram. These composables are used in the corresponding activity's layout.


### Backend Class Diagram
![BackEnd Class Diagram](https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/19504579/d767a7ef-27b5-480d-b040-5cc9fea5cd2f)

The backend class diagram consists of mainly three parts, which are `posts`, `tags`, `users`. These parts represents three internal apps of Django, each consisting several relevant model classes.

- App `posts` contains `Post`, `PostPhotos`, `Restaurant` models.
- App `tags` contains `Tag` models.
- App `users` contains `User`, `Follow` models.

These models are connected with each other through foreign key relationships. For example, `Post` model has a foreign key to `Restaurant` model, and `PostPhotos` model has a foreign key to `Post` model. These relations are represented as arrows in the diagram.

## Implementation Details

### Frontend Views

#### Landing Page, Log-In Page, Sign Up Page

<div style="display: flex">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/ca7f16c1-7f54-477b-8a4c-6f06693e19d3">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/b730b351-c8c3-4ef2-94a5-7a9f25eb1066">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/ee1a18e8-f0f0-4382-9e8d-9a271fcefb4e">
</div>

* **Landing Page**
  > - This is the landing page. It redirects to the login screen automatically after 1 second.

* **Log-In Page**
  > - On this page, you can log in by entering your username and password.
  > - If the login information is incorrect, a toast message indicating login failure will appear.
  > - If you don't have an account, you can navigate to the sign-up screen using the signup button.

* **Sign Up Page**
  > - This is the sign-up page. You can sign up by providing your email, username, and password.
  > - The password visibility toggle button on the right enhances user convenience.
  > - If the entered information is incorrect or doesn't meet the requirements, relevant toast messages will appear.
  > - Upon successful registration, you will be automatically logged in and redirected to the home screen.




#### Splash Screen

<div style="display: flex">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/5055e863-e251-4f9b-ba4a-830b4708f7ab">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/e4a74652-3705-449d-b7e9-74645986c212">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/62f3b04a-3a7c-465a-809d-5f0afd1c7896">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/60e98f27-6223-44bf-a35a-2ce511c7fa6f">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/f403c1db-a3e6-4df2-900a-4429e8c32e56">

</div>




* **Splash Screen**

  > - This screen appears exclusively for first-time users (newly signed up), introducing them the app's features.
  > - The app uses AI to analyze users' reviews and preferences, creating personalized tags for tailored recommendations.




After logging in to the app, you can navigate to the home page, search page, upload page, and your profile page using the bottom navigation bar.



#### Home Page

<div style="display: flex">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/96be8c26-9ca6-4b63-9bce-9c26b96a645f">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/26f8de9a-62d5-46d3-b147-6be3e31bdb3f">
</div>



* **Home Page**

  > - This is the home page. It provides two types of feed for the user.
  > - The first type is a personalized recommendations of reviews from users with similar tastes(tags).
  > - The second type consists of reviews from other users the current user follows.
  > - Each review includes the uploader's profile, name, one-line introduction, photos, rating, restaurant name, review description, like count, and like status of loginned user.
  > - Photos of each reviews can be scrolled horizontally, and all reviews can be scrolled vertically. You can see larger image pop-up by clicking on each photo 
  > - You can navigate to search page, upload page, and your profile page using the bottom navigation bar.




#### User Profile page, Edit Profile page

<div style="display: flex">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/cb94c65e-d558-4b05-a5e8-b7c1c8b0d61f">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/9ff34aad-785d-468c-9574-d4a598dfe9a6">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/6e899637-d734-4b58-83dd-e47697197ada">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/a0150812-8e85-4a50-8be1-b2b2933ee824">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/9476cdf7-ff51-4020-9a26-e2eecfa29dfa">
</div>




* **User Profile Page**

  > - This is the user profile page. It provides 1) profile information and 2) two types of feed for the user. There are MY feed for posted reviews, and LIKED feed for saving reviews you pressed Like.
  > - Aside the profile, there are follower and following information, and clicking on them shows list of followers and followings respectively. Also, there is "프로필 편집" button that triggers navigation to the edit profile page. 
  > - Personalized tags for each users are displayed below. If the user clicks the "태그 갱신" button under the tags, personalized tags based on the user's reviews will be updated.
  > - If this is profile of another user, there is follow toggle button instead of "프로필 편집", showing following status. Also, only posted reviews can be seen, not liked. Lastly, there will be no "태그 갱신" button.
  > - Finally, you can scroll vertically to see reviews.
  > - You can navigate to search page, upload page, and home page using the bottom navigation bar.


* **Edit Profile Page**
  > - In the edit profile page, you can either edit your profile image and one-line description, or log-out.





#### Search Page

<div style="display: flex">
  <img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/ec016d9e-461d-4f3e-a63c-f5b12dec638f">
  <img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/63f893f3-f8cf-42b2-9326-995052b475cb">
  <img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/68dfbd38-e775-44f0-8f59-4a865a223a74">
  <img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/02b2ea90-494d-4b60-baf7-c0c6ca3bdf6a">
  <img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/b01a3b55-4e29-46d0-9f7e-b332eadceaeb">
</div>

* **Search Page**

  > - This is the search page. You can choose a category you want to search (out of "유저", "태그", "식당") by pressing a button under the search bar. Every search can be done on value change of search text, pressing Enter, or clicking the search icon.
  > - "유저" mode searches users by user id. All users containing the search text are shown.
  > - "태그" mode searches users by user tags. Unlike user id, tag must be exact match.
  > - "식당" mode searches posts by restaurant names. All posts about restaurants containing the search text are shown.
  > - You can navigate to home page, upload page, and your profile page using the bottom navigation bar.



#### Review Upload Page

<div style="display: flex">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/220f6013-153f-4f17-b8c8-858288153915">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/f7cdaaea-7ca6-49e3-ad1c-73c5e2a3a0b5">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/7a172cd2-af70-41f6-b942-8f09c36059bf">
<img width="280" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/bac5ab92-42e6-421d-a095-0f8f0caf4815">
</div>


* **Review Upload Page**

  > - This is the review upload page. You can upload a review by adding photos from your gallery.
  > - By clicking the "식당 검색" button, you can search for the restaurant you want to review and select it.
  > - After adding ratings and review description, you can upload the review by clicking the "리뷰 작성" button at the bottom. Photos are optional.



### Backend API

#### Users API


##### Endpoint: POST `/users/login`

- Login a user and get a token
- Request:
```typescript
interface UserLoginRequest {
  username: string;
  password: string;
}
```
- Response:
```typescript
interface UserLoginResponse {
  token: string;
}
```

##### Endpoint: POST `/users/register`
- Register a new user
- Request:
```typescript
interface UserRegisterRequest {
  email: string;
  username: string;
  password: string;
}
```
- Response:
```typescript
interface UserLoginResponse {
  token: string;
}
```

##### Endpoints: GET `/users/me/`, GET `/users/{user_pk}/`
- Get user profile (own profile or other user's profile)
- Request:
```typescript
interface GetUserProfileRequest {
  // Empty request body
}
```
- Response:
```typescript
interface User {
  id: number;
  username: string;
  email: string;
  description: string;
  avatar_url: string;
  follower_count: number;
  following_count: number;
  posts: Post[];
}

interface GetUserProfileResponse {
  data: User;
}
```
##### Endpoint: PATCH `/users/edit/`
- Edit user profile
- Request:
```typescript
interface EditUserProfileRequest {
  description?: string; // Optional: New description
  avatar_url?: string; // Optional: New avatar URL
}
```
- Response:
```typescript
interface EditUserProfileResponse {
  id: number;
  username: string;
  email: string;
  description: string;
  avatar_url: string;
  follower_count: number;
  following_count: number;
  posts: Post[];
}
```

##### Endpoint: POST `/users/{id}/follow/`
- Follow a user
- Request:
```typescript
interface FollowUserRequest {
  // Empty request body
}
```
- Response:
```typescript
interface FollowUserResponse {
  following: boolean; // Updated following status
}
```


##### Endpoint: GET `/users/{id}/followers/`, `/users/{id}/followings/`
- Get followers/followings of a user
- Request:
```typescript
interface GetUserFollowersRequest {
  // Empty request body
}
```
- Response:
```typescript
interface User {
  id: number;
  username: string;
  email: string;
  description: string;
  avatar_url: string;
  follower_count: number;
  following_count: number;
  posts: Post[];
}
```

##### Endpoint: GET `/users/me/liked-posts/`
- Get liked posts of a user
- Request:
```typescript
interface GetUserLikedPostsRequest {
  // Empty request body
}
```
- Response:
```typescript
interface Post {
  id: number;
  restaurant: {
    id: number;
    name: string;
  };
  photos: {
    id: number;
    photo_url: string;
    post: number;
  }[];
  user: {
    username: string;
    id: number;
    avatar_url: string;
    description: string;
  };
  rating: string;
  description: string;
  created_at: string; // ISO 8601 format
  is_liked: boolean;
  like_count: number;
}
```

##### Endpoint: GET `/users/posts/{id}/`
- Get posts of a user
- Request:
```typescript
interface GetUserPostsRequest {
  // Empty request body
}
```
- Response:
```typescript
interface Post {
  id: number;
  restaurant: {
    id: number;
    name: string;
  };
  photos: {
    id: number;
    photo_url: string;
    post: number;
  }[];
  user: {
    username: string;
    id: number;
    avatar_url: string;
    description: string;
  };
  rating: string;
  description: string;
  created_at: string; // ISO 8601 format
  is_liked: boolean;
  like_count: number;
}
```

##### Endpoint: POST `/users/refresh-tags/`

- Refresh user tags

- Request:
```typescript
interface TagRefreshRequest {
  // Empty request body
}
```
- Response:
```typescript
interface TagRefreshResponse {
  user_tags: List<String>; 
  removed: List<String>
  added: List<String>
}
```


##### Endpoint: GET `/users/filter`
- Search users by username or tags
- Request:
```typescript
interface GetUserProfileRequest {
  username: string; // Required: Username for filtering
}
```
- Response:
```typescript
interface User {
  id: number;
  username: string;
  email: string;
  description: string;
  avatar_url: string;
  follower_count: number;
  following_count: number;
  posts: Post[];
}

interface GetUserProfileResponse {
  data: User;
}
```


#### Posts API

##### Endpoint: GET `/posts/`
- Get posts
- Request:
```typescript
interface searchByRestaurant {
  restaurant_name: String
}
```
- Response:
```typescript
interface Response {
  id: number; // ID of the created post
  restaurant: {
    id: number;
    name: string;
  };
  photos: {
    id: number;
    photo_url: string;
    post: number;
  }[];
  user: {
    username: string;
    id: number;
    avatar_url: string;
    description: string;
  };
  rating: string;
  description: string;
  created_at: string; // ISO 8601 format
  is_liked: boolean;
  like_count: number;
  tags: List<String>
}
```

##### Endpoint: POST `/posts`
- Create a post
- Request:
```typescript
interface CreatePostRequest {
  restaurant_id: number;
  photos: {
    photo_url: string;
  }[];
  rating: string;
  description: string;
}
```
- Response:
```typescript
interface CreatePostResponse {
  id: number; // ID of the created post
  restaurant: {
    id: number;
    name: string;
  };
  photos: {
    id: number;
    photo_url: string;
    post: number;
  }[];
  user: {
    username: string;
    id: number;
    avatar_url: string;
    description: string;
  };
  rating: string;
  description: string;
  created_at: string; // ISO 8601 format
  is_liked: boolean;
  like_count: number;
  tags: List<String>
}
```

##### Endpoint: DELETE `/posts/{id}/`
- Delete a post
- Request/Response: Both empty

##### Endpoint: GET `/posts/following/`, `/posts/recommend/`
- Get posts of users that the current user follows (/posts/following/)
- Or get recommended posts for the current user (/posts/recommend/)
- Request:
```typescript
interface GetFollowingPostsRequest {
  // Empty request body
}
```
- Response:
```typescript
interface Post {
  id: number;
  restaurant: {
    id: number;
    name: string;
  };
  photos: {
    id: number;
    photo_url: string;
    post: number;
  }[];
  user: {
    username: string;
    id: number;
    avatar_url: string;
    description: string;
  };
  rating: string;
  description: string;
  created_at: string; // ISO 8601 format
  is_liked: boolean;
  like_count: number;
}
```

##### Endpoint: GET `/posts/restaurant-search`
- Search restaurants by name in the process of creating a post
- Request:
```typescript
interface getSearchRest {
  query: String
  x: String
  y: String
}
```
- Response:
```typescript
interface Response {
  rest: {
  id : Int,
  place_name : String,
  road_address_name : String,
  category_name : String,
  x : String,
  y : String,};
}
```

##### Endpoint: PUT `/posts/{post_id}/likes`
- Request:
```typescript
interface ToggleLikeRequest {
  // Empty request body
}
```
- Response:
```typescript
interface ToggleLikeResponse {
  is_liked: boolean; // Updated like status
  like_count: number; // Updated like count
}
```


#### Image API

##### Endpoint: POST `/image/upload`
- Upload an image to S3 and get the image URL
- Request:
```typescript
interface ImageUploadRequest {
  image: File; // The image file to upload
}
```
- Response:
```typescript
interface ImageUploadResponse {
  image_url: string; // URL of the uploaded image
}
```



#### Tags API

##### Endpoint: GET `/tags/top-tags/`
- Get top tags of current posts over all users (used in search screen)
- Request:
```typescript
interface GetTopTagsRequest {
  // Empty request body
}
```
- Response:
```typescript
interface GetTopTagsResponse {
  tags: TopTag[]; // List of top tags
}

interface TopTag {
  ko_label: string; // Korean label of the tag
  en_label: string; // English label of the tag
  type: string; // Type of the tag
}
```

# Testing Plan

## Unit Testing & Integration Testing

### Android App [ Frontend ]

For android app, we are using JUnit 4 for unit testing and integration testing. Additionally, we are using mockk for mocking objects and coroutines for asynchronous testing. 
We are focusing on testing the business logic of the app, which is mainly implemented in the ViewModel. Integration tests are also performed to check interactions between ViewModels and the Repository, ensuring correct API communication with the backend and proper data flow throughout the app.

### Django App [ Backend ]

For Django app, we are using unit test library and django testing library for API testing. We have 80% of code coverage over important code, excluding migration files. We are excluding migration files because they are automatically generated by Django and are not part of our codebase. Especially for all views.py, models.py files, we accomplished more coverage than 75%.

### Test Coverage Report 

| As shown below in Figure 1 we tested `13` methods for StartViewModel in start package & `50` methods for AppMainViewModel in appmain package.

<img width="1427" alt="overall_white" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144414644/29d04e3e-403f-400e-8e48-6aad08d7a5e6">

**Figure1. Overall Coverage Report**

| As shown below in Figure 2 & 3 , we covered `92.9%` for StartViewModel & `89.6%` for AppMainViewModel.

<img width="1426" alt="start_white" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144414644/47ceb9b8-a495-4876-9761-ca02898672d9">

**Figure2. StartViewModel Coverage Report**




<img width="1427" alt="appmain_white" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144414644/0926535f-7101-4487-af0a-511c9aa7d968">

**Figure3. AppMainViewModel Coverage Report**




We used Jetpack’s Composable functions across all ScreenKt Classes. Composables are a modern toolkit for building native UIs in Android. They don't need a unit test for the following reasons.
 * Composables are declarative which is they describe what the UI should look like for a given state, rather than detailing the steps to construct the UI.
 * Composables are side-effect-free and only responsible for UI rendering based on the inputs they receive.
 

Therefore, they **don't** contain logic that needs to be verified through unit tests. 


So, we replaced them through an integration test that verifies the interaction between different parts of our app using Compose Testing Library. It is shown below in Figure4.




<img width="670" alt="Screenshot 2023-12-09 at 1 17 11 AM" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144414644/ca6e9ba7-65fe-4f8b-8053-a8a4452ee787">



**Figure4. Integration Test Coverage Report**



<img width="187" alt="Screenshot 2023-12-09 at 1 03 52 AM" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/144414644/99d3a4f1-afea-4274-b692-28d0eca78285">

**Figure5. Backend Test Coverage Report**



## Acceptance Testing  

We plan to test 5 user stories for User Acceptance Testing. This stories are also listed in Requirements & Specifications.

1. As a user, I want to search for other users by their usernames or user tags, so I can connect with people who share similar food tastes with me.
2. As a user, I want to write and post reviews for restaurants, including details like the restaurant's name, photos, ratings, and comments, so I can share my dining experiences with others.
3. As a user, I want to receive personalized tags at my profile based on my previous reviews, so my profile appropriately represents my food tastes.
4. As a user, I want to record my dining history by reviews and view those of others' that I liked, so that I can revisit and go down my memory lane.
5. As a user, I want to follow other users that has similar tastes with me, so I can keep up with their reviews at home feed.

## ML Accuracy Testing
- **Dataset**: Reviews from Naver, Kakao, generated by ChatGPT, and personal reviews (Total: 40)
- **Objective**: Heuristically select appropriate atmosphere tags for each review (choose two in ambiguous cases).
- **Success Criteria**: Test passes if the selected tag appears in the review.

### First Testing Phase
- **Total Reviews Tested**: 27
- **Failures**: 6
  - **Issue**: The tag 'friendly service' was too generic and applied to many reviews.
  - **Solution**: Modified the tag to 'excellent staff service' and lowered the score threshold from 0.3 to 0.23.
  
### Second Testing Phase
- **Total Reviews Retested**: 27
- **Failures**: 1
  - **Improvement**: Significant decrease in failures post-adjustments.

### Additional Testing
- **New Total Reviews Tested**: 40
- **Failures**: 2
  - **Result**: Majority (38/40) passed, indicating successful tag application and improved algorithm performance.

**Conclusion**: The adjustments to the tag and score threshold resulted in more accurate and relevant tag application in the reviews.


<div style="display: flex">
  <img width="840" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/223e5e7c-9d76-4582-ac50-002652ac465b">
  <img width="840" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/1dab82fa-6d18-4657-8487-951071ba2d4e">
  <img width="840" alt="api" src="https://github.com/snuhcs-course/swpp-2023-project-team-15/assets/89760986/a77dfad0-fb90-403d-94f9-ae6bb7d1d881">
</div>







