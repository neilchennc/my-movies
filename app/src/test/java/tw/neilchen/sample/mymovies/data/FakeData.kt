package tw.neilchen.sample.mymovies.data

object FakeData {

    val movie = Movie(
        adult = false,
        backdropPath = "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
        genreIds = listOf(123, 456),
        id = 238,
        originalLanguage = "en",
        originalTitle = "The Godfather",
        overview = "40年代的美國，“教父”維托·唐·柯里昂是黑手黨柯里昂家族的首領，帶領家族從事非法的勾當，但同時他也是許多弱小平民的保護神，深得人們愛戴。 因為拒絕了毒梟索洛索的毒品交易要求，柯里昂家族和紐約其他幾個黑手黨家族的矛盾激化。聖誕前夕，索洛索劫持了“教父”的參謀湯姆，並派人暗殺“教父”；因為內奸的出賣，“教父”的大兒子遜尼被仇家殺害；小兒子麥克也被捲了進來，失去愛妻。黑手黨家族之間的矛盾越來越白熱化。 年老的“教父”面對喪子之痛怎樣統領全局？黑手黨之間的仇殺如何落幕？誰是家族的內奸？誰又能夠成為新一代的“教父”？ 血雨腥風和溫情脈脈，在這部里程碑式的黑幫史詩巨片裡真實上演。",
        popularity = 134.96,
        posterPath = "/y03tzUKvkRCYwJ5NWys4W4bnS9m.jpg",
        releaseDate = "1972-03-24",
        title = "教父",
        video = false,
        voteAverage = 8.689,
        voteCount = 1
    )

    val movieDetail = MovieDetail(
        adult = false,
        backdropPath = "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
        belongsToCollection = BelongsToCollection(230, "教父（系列）", "", ""),
        budget = 6000000,
        genres = listOf(Genre(18, "劇情"), Genre(80, "犯罪")),
        homepage = "",
        id = 238,
        imdbId = "tt0068646",
        originCountry = arrayListOf("US"),
        originalLanguage = "en",
        originalTitle = "The Godfather",
        overview = "40年代的美國，“教父”維托·唐·柯里昂是黑手黨柯里昂家族的首領，帶領家族從事非法的勾當，但同時他也是許多弱小平民的保護神，深得人們愛戴。 因為拒絕了毒梟索洛索的毒品交易要求，柯里昂家族和紐約其他幾個黑手黨家族的矛盾激化。聖誕前夕，索洛索劫持了“教父”的參謀湯姆，並派人暗殺“教父”；因為內奸的出賣，“教父”的大兒子遜尼被仇家殺害；小兒子麥克也被捲了進來，失去愛妻。黑手黨家族之間的矛盾越來越白熱化。 年老的“教父”面對喪子之痛怎樣統領全局？黑手黨之間的仇殺如何落幕？誰是家族的內奸？誰又能夠成為新一代的“教父”？ 血雨腥風和溫情脈脈，在這部里程碑式的黑幫史詩巨片裡真實上演。",
        popularity = 134.96,
        posterPath = "/y03tzUKvkRCYwJ5NWys4W4bnS9m.jpg",
        productionCompanies = listOf(
            ProductionCompany(4, "", "Paramount Picture", "US"),
            ProductionCompany(10211, "", "Alfran Production", "US")
        ),
        productionCountries = listOf(ProductionCountry("US", "United States of America")),
        releaseDate = "1972-03-24",
        revenue = 245066411,
        runtime = 175,
        spokenLanguages = listOf(
            SpokenLanguage("English", "en", "English"),
            SpokenLanguage("Italian", "it", "Italiano"),
            SpokenLanguage("Latin", "la", "Latin")
        ),
        status = "Released",
        tagline = "An offer you can't refuse.",
        title = "教父",
        video = false,
        voteAverage = 8.689,
        voteCount = 211123
    )

    val movieCredits = MovieCredits(238, emptyList(), emptyList())

    val movieImages = MovieImages(238, emptyList(), emptyList(), emptyList())

    val movieVideos = MovieVideos(238, emptyList())

    val personDetail = PersonDetail(
        adult = false,
        alsoKnownAs = listOf(),
        biography = "",
        birthday = "1999-12-31",
        deathday = "",
        gender = 0,
        homepage = "",
        id = 999,
        imdbId = "",
        knownForDepartment = "",
        name = "TODO",
        placeOfBirth = "",
        popularity = 100.0,
        profilePath = ""
    )

    val personImage = ProfileImage(
        aspectRatio = 1.667f,
        width = 1920,
        height = 1080,
        iso6391 = "",
        filePath = "",
        voteAverage = 10.0,
        voteCount = 1000,
    )

    val personMovieCredits = PersonMovieCredits(id = 999, cast = emptyList(), crew = emptyList())
}