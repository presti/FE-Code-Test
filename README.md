# Code Challenge
-by Javier Presti

A) Describe the strategy used to consume the API endpoints and the data management.

The list endpoint is consumed to fetch the items, and then on demand the other endpoint is used to fetch the ingredients. When we go to an item's detail, we fetch the detail again (See point D for comments on this). The list is being shown when we get the response from the first endpoint, while we are still fetching the image and ingredients.
The cocktail items are being stored in a list (not persisted) and when we get the detail of a cocktail, the item in the list is being updated and the observer(s) notified. Having in mind that RecyclerView recycles views, we can safely use the updated item.

B) Explain which library was used for the routing and why. Would you use the same for a consumer facing app targeting thousands of users? Why?

Retrofit was used to consume the API because of its simplicity and automatic parsing of responses. However, this last feature was in the end not used in this project as a manual parsing was required because of how the endpoints were made.
Server-wise, it is safe to use this library for thousands of users as long as you are caching responses with OkHttp. Otherwise, I would use Volley since it provides caching of requests.

C) Have you used any strategy to optimize the performance of the list generated for the first feature?

RecyclerView was used to list the items and Glide to fetch and show the images. Both are specially good at handling a list of items. Apart from this, the images and ingredients (which are obtained from the details endpoint) are fetched on demand (only when we want to see the item).
We are using Glide default's caching for storing images and the details of each item are being kept in the item's instance. This way we prevent fetching again elements.

D) Would you like to add any further comments or observations?

Yes. As this is a test, there are some concerns that should be taken into account:
- We should cache the responses we get, so we will not make a network request again during a set interval. Instead of adding cache to Retrofit, I would instead use Volley in this case. This would also automatically prevent making a network request again when we go to an item's detail.
- We should store the elements, so when we try to fetch again, we have something (old) to show. And then we should only update the items that were changed in the list, if any.
- The Search state is not being stored in the lifecycle, so for example a text entered in the search field will be lost when rotating the device. I would fix this.
- While searching, when matching, for simplicity I am converting each item name to lowercase. This is done each time we trigger the search (on each keyboard input). I would have a separate list containing the lowercase of the elements, that we can for example create the first time the search is triggered, so for the next input we don't have to do that computation. If the list if big enough, we can also have the list stored.


## Overview:

Implement a simple mobile cocktails catalogue (master / detail). The catalogue consists of a table view list of cocktails with their name, toppings and photo. Once the user taps on a specific row it will push a new screen with that drink’s details: Name, Photo, Ingredients and Preparation.


## Features:

**1. Cocktails list:**

For each row of the list it will display the Cocktail name and photo (See wireframe 1).
The API endpoint that should be consumed for this purpose is: 

http://www.thecocktaildb.com/api/json/v1/1/filter.php?g=Cocktail_glass

This returns a JSON list of cocktails, and the information needed in order to populate each row of the list.

```
{
 	strDrink,           → Cocktail name
     	strDrinkThumb,  → Photo URL
      	idDrink       → Cocktail ID
}
```

Wireframe 1:

![screen shot 2018-02-02 at 12 53 57](https://user-images.githubusercontent.com/263229/35742087-40b1ce26-0818-11e8-91d7-5c2ea0d4a6aa.png)




**2. Cocktail detail:**

Once the user taps on a row from the list mentioned in the previous feature it will push a new screen with the selected cocktail’s details, where it will show it’s name, photo, ingredients and instructions (See wireframe 2)

The endpoint to be used for this is the following:
 
http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=${idDrink} → Cocktail ID
I.g.: http://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=16108

The endpoint returns a JSON with the cocktails info, the needed properties are:
```
{
	strInstructions,  → instructions
	strDrink,         → cocktail name
	strDrinkThumb,    → photo URL
	strIngredient1,   → ingredient 1
	...
	strIngredientN    → ingredient N
}
```

Wireframe 2

![screen shot 2018-02-02 at 12 53 37](https://user-images.githubusercontent.com/263229/35742155-63205b1c-0818-11e8-8b4b-608a46eaa718.png)
	
  
  
  
**3. Bonus Points: (Optional)**

Implement a filter by name functionality on the first screen that automatically filters the results while typing, only showing the rows that satisfy the criteria entered by the user.


