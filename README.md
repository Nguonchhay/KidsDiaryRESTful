# Welcome to Kids Diary RESTful API

Kids Diary App is a RESTful API that was built using Java Spring technologies. This is a project to finish
Advanced Java course.

# Maintainers

- Touch Nguonchhay
- Tes Putthira
- Ly Mengkong
- Kour Sotheareach
- Mok Vanvannak

# Development environment

- maven 3
- jdk java8
- Eclipse (recommended). In order to open project with Eclipse, run this command `mvn eclipse:eclipse`.

# Local development

- Clone project from github: `git clone https://github.com/Nguonchhay/KidsDiaryRESTful.git kids-diary`
- Go to root directory of project: `cd kids-diary`
- Create database name `kidsdiary` and import database structure from `kids-diary/src/main/resources/kidsdiary.sql`

# Starting up server
	
	mvn clean jetty:run

Then open your browse with url `http://localhost:8080/index.html`.

# RESTful API Document

	+ UserType

		- List
			* Request URL: `/api/usertypes/v1/all`
			* Method     : GET
	+ Country
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

Hope you can run this back end project successfully.
