# Welcome to Kids Diary RESTful API Document

# UserType

	- List
		* Request URL: `/api/usertypes/v1/all`
		* Method     : GET

# Country
	- List
		* Request URL: `/api/countries/v1/all`
		* Method     : GET
		* Response   :
						`````````````````````````````````````````````
						[
						  {
						    "id": 1,
						    "createdAt": "2017-02-23T23:15:26+0700",
						    "name": "Cam",
						    "dialingCode": " 88"
						  }
						]
						`````````````````````````````````````````````
	- Create
		* Request URL: `/api/countries/v1`
		* Method     : POST
		* Params     :
						```````````````````````
						{
							name: string,
							dialingCode: string
						}
						```````````````````````
	- Update
		* Request URL: `/api/countries/v1/{id}`
		* Method     : PUT
		* Params     :
						```````````````````````
						{
							name: string,
							dialingCode: string
						}
						```````````````````````
	- Delete
		* Request URL: `/api/countries/v1/{id}`
		* Method     : DELETE
	- Force delete
		* Request URL: `/api/countries/v1/force/{id}`
		* Method     : DELETE

# Family
	- List
		* Request URL: `/api/families/v1/all`
		* Method     : GET
		* Response   :
						`````````````````````````````````````````````
						[
						  {
						    "id": Long,
						    "father": Long,
						    "mother": Long,
						    "child": Long,
						    "note": String
						  }
						]
						`````````````````````````````````````````````
	- Create
		* Request URL: `/api/families/v1`
		* Method     : POST
		* Params     :
						```````````````````````
						{
						    "father": Long,
						    "mother": Long,
						    "child": Long,
						    "note": String
						}
						```````````````````````
	- Update
		* Request URL: `/api/families/v1/{id}`
		* Method     : PUT
		* Params     :
						```````````````````````
						{
							"father": Long,
						    "mother": Long,
						    "child": Long,
						    "note": String
						}
						```````````````````````
	- Delete
		* Request URL: `/api/families/v1/{id}`
		* Method     : DELETE
	- Force delete
		* Request URL: `/api/families/v1/force/{id}`
		* Method     : DELETE

Hope you can request these RESTful apis successfully.
