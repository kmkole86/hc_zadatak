
Task: Paginated search with support for the offline mode done in a non blocking (Ui) manner

Libs used: Ktor, Hilt, Compose, Flow, Room

Architecture: layered, simple clean, domain, data, presentation. Haven’t used UseCases since for this 
App it would be just connection between repository and ui with no deeper meaning or function

Features: 

		Caching: all of the data from api are stored in local database,SearchResult and PlaceDetails also.

		Tables: 
                //search
		PlaceDb Table where all of the search result are saved
		PlaceDbSearchIndex Table where the ids of current search result are stored.

		//favourites
		FavouritePlaceIdDb Table where IDs of the favorite places are stored. 

		//details
		PlaceDetailsDb Table (enhanced PlaceDb) where PlaceDetails are cached. 
		PlaceDetailsDb is considered a different model than the one returned by search.

		Ui is observing
		 Cache(PlaceDb Table) INNER JOIN SearchIndex(PlaceDbSearchIndex Table) LEFT JOIN Favourites(FavouritePlaceIdDb Table)

		ONLINE SEARCH
		1: user type some query
		2: result successfully fetched
		3: places from result are stored in PlaceDb Table
		4: place ids of the current search page  are stored into PlaceDbSearchIndex Table

		OFFLINE SEARCH:
		app will not reach api for result, instead it will search local Cache(PlaceDb Table) and just
		update the SearchIndex (PlaceDbSearchIndex Table)

		UI will be reactively updated since the only difference Online/Offline is where Ids for SearchIndex are coming from ( api or 				local cache search )

		Online/Offline mode support: there are lot of approaches, generally it should be monitored, and to update and notify user
		about the current state and handle transition gracefully, I didn’t have time to implement everything,
		 just for the sake of the task I have mocked the simplest posible behavior. 

		To emulate offline behavior, go online  >> search, also, open some place details to fill the cache. Then change return value in the 
		>>DeviceNetworkDataSourceImpl<< to false


		Merging of the ApiResult, CachedData, Favourites status etc. can be done In a range from presentation to the data layer.
		Every approach has its pro/cons. I have decided to put most of the implementation details on the periphery of the app (data layer), 		
    so that the rest like Presentation can remain relatively simple and easy to read and maintained. Not so granular update like it would
		be if we e.g. observe Favorite status, Place data etc, separately in the view model.

		Since this is a simple app, hard to define "right level of solution" in the range from “just to work, do it asap” to “over engineering,”
		Since it can be done without DI, without reactive approach, without a lot of the things. anyway I have done it like it 
		Will be long term developed app, so it’s a little over engineered.

		 Main focus Is on architecture and solution, UI is there just for the proof of concept, didn’t have lot of time for implementation, 
		due to the nature of the project I am currently working on, I am obligated to work from office so my day passes in the 
		commuting to/from and in the office.

		If you have any questions don’t hesitate to contact me, i will be more than happy to answer it. Tanks for your time!!!
