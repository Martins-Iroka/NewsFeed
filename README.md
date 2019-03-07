# NewsFeed

NewsFeed is an application that gives a user regularly-updated news from the internet. The application connects to newsapi.org using
news api to get recent news. The application gets news based on category - such as business news, sport news, etc.
Also, it gets news base on country, e.g Nigeria, United States, United Kingdom, Canada, and Australia
Android frameworks were used to make the app user-friendly, such as:
  1. RecyclerView - to display the news feeds.
  2. Navigation Drawer - for the options/category.
  3. SwipeToRefresh - to refresh the recyclerView with news feeds.
  4. WebView - to show news information using the news url.
  5. AsyncLoader - to handle background task such as connecting to the internet and fetching news feeds.
  6. Picasso - an external library used to handle loading of image url from the internet.
