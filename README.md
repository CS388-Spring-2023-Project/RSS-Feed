# Milestones
## Table of Contents

1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Sprint 1](#Sprint-1)
1. [Sprint 2](#Sprint-2)
1. [Sprint 3](#Sprint-3)
1. [Sprint 4](#Sprint-4)
## Overview

### Description

RSS feed is an app that allows users to subscribe to multiple websites via RSS in order to see updates to each in real time. Users can scroll through a feed of every website they are subscribed too; tapping an article opens it in the user's web browser. Users also have access to a settings menu that lets them subscribe to/unsubscribe from websites and manage their account.

### App Evaluation

   - **Description:** Users can subscribe to one or more websites, and be served a feed of articles posted to all subscribed websites. Each article has a brief description, and tapping the article opens it in the user's we browser.
   - **Category:** Entertainment
   - **Mobile:** Users can conveniently access content from anywhere. Additionally, any people use their phones as their primary way to access the internet. Finally, users can see articles being posted faster if they can access the feed through their phone.
   - **Story:** Anyone who follows multiple sites will love having a centrilized feed.
   - **Market:** Anyone who follows multiple new sites, or other site that uses RSS (such as podcasts).
   - **Habit:** Users will get on consistantly to check the news and see what new articles/media have come out.
   - **Scope:** Moderate. This app relies mainly on things that we have bee taught (RecyclerViews, displaying websites, etc). The main complication is accessing APIs; if RSS doesn't have an applicable API (i.e., one that allows for updates from arbitrary sites to be tracked), the scope may have to be limited to apps that we have implemented.

## Product Spec

### 1. User Features (Required and Optional)

**Required Features**

* [x] User can log in.
* [X] User can create an account.
* [X] Users can subscribe to websites through RSS (fallback; through API).
* [X] Users can see their subscription feed.
* [X] Users can manage their account (log out, delete account).
* [x] Users can *un*subscribe from websites.
* [X] Users can access articles through the feed by tapping on them.
* [ ] Users can change themes of applications.
* [ ] Filter feed by source, content type (article, podcast, etc), etc.

**Stretch Features**

* [ ] "Real time" updates (app checks for updates in real time and tells users when there is something new if they refresh; like the tumblr app).
* [ ] Push notifications when websites are updated (can be adjusted on a per-website basis).
* [ ] Users can see the most popular subscriptions by other app users.


### 2. Screen Archetypes

- Login Screen
  - Users can log in.
- Registration Screen
  - Users can make an account.
- Main Feed
    - Users can see all their subscriptions.
    - Users can open articles.
- Settings
    - Subscriptions
        - Users can add/remove subscriptions.
    - Account Management
        - Users can manage/delete their account.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Main Feed
* Settings

**Flow Navigation** (Screen to Screen)

- Login Screen
  - Main Feed
- Registration Screen
  - Main Feed
- Main Feed
    - Article (in web browser)
- Settings
    - Subscriptions
    - Account Mangement
        - Login Screen (on logging out/deleting account)


## Wireframes
<img src="https://user-images.githubusercontent.com/69495267/226485966-8dd98a82-4496-464f-a1c7-17e11961edee.jpg" width=600>


### [BONUS] Digital Wireframes & Mockups
<img src='https://github.com/CS388-Spring-2023-Project/RSS-Feed/blob/main/wireframe_1.jpg'>
### [BONUS] Interactive Prototype

# Sprint 1
## User can Login and Create Account
![Untitled](https://user-images.githubusercontent.com/62580207/228110034-66188a0f-2429-4a2d-81d2-0bb6736f6d44.gif)

# Sprint 2
## User can Subscribe to RSS Feeds and see their subscription feed.
![part2](https://user-images.githubusercontent.com/62580207/229656554-cdb074e8-2072-4b08-a110-1d475fd101ca.gif)

# Sprint 3 
## Users can manage their account (log out, delete account), and *un*subscribe from Feeds.
![Untitled](https://user-images.githubusercontent.com/62580207/231009844-a7516bb3-2052-44cf-94b0-eca2bc41ae82.gif)

# Sprint 4 
## Users can access articles through the feed by tapping on them.
![part3](https://user-images.githubusercontent.com/62580207/232624633-6b105b38-d695-4f8c-a6ad-e8b966049c8c.gif)

# Sprint 5
## Users can Filter their favorite articles
![final](https://user-images.githubusercontent.com/62580207/235270999-7b1974de-09d1-4146-8e5f-0949d823d0f4.gif)
