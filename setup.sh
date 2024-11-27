#!/bin/sh

replace_in_file() {
  local value="$1"
  local regexp="$2"
  local file="$3"

  if [ -n "$value" ]; then
    sed -i "s|$regexp|" "$file"
  fi
}

# Define light blue color
LIGHT_BLUE='\033[1;34m'
NC='\033[0m' # No Color


 echo '                                     .@@@@               @@@@                                      '
 echo '                                 .@@                          @@                                   '
 echo '                              .@@  #@                          @%                                  '
 echo '                             @@@@@@@@                           @                                  '
 echo '                             @@@@@@@@.                          @#                                 '
 echo '                             @@@@@@@@@                           @                                 '
 echo '                            .@@@@@@@@@                    @      @*                                '
 echo '                            *@@@@@@@@@. +@@@@@@@@@@@@@@@@@@@@@@@@ @                                '
 echo '                            @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                '
 echo '                            @@@@@@@@@@@@@@@@@@@@@@      @    .@@@@                                 '
 echo '                            @@@@@@@@@      @@@@@@@               @                                 '
 echo '                            @@@@@@@@@   @@@@@@@@@@        @@@@@@. @                                '
 echo '                            @@@@@@@@@. @@      @@@        @       @.                               '
 echo '                            @@@@@@@@@@         %@@@                @                               '
 echo '                            @@@@@@@@@@         -@@@         .      =@                              '
 echo '                            @@@@@@@@@@:        =@@@                 @                              '
 echo '                            @@@@@@@@@@@        *@@@@%*=*@@@@@       @                              '
 echo '                            @@@@@@@@@@@         @@@@@@@@@@@@@       @                              '
 echo '                            @@@@@@@@@@@@         @@@@@@@@@#         @                              '
 echo '                              @@@@@@@@@@                            @#                             '
 echo '                                @@@@@@@@                     .      @@                             '
 echo '                                @@@@@@@@            @@@@@@@@        @@                             '
 echo '                                -@@@@@@@@                           %@                             '
 echo '                                   %@@@@@@    .                -@@@@@-                             '
 echo '                                    @@@@@@@@@@@@@@@@@@@@@@@@@@@@%                                  '
 echo '                                   @@@@@@@@@@@@@@@@@@@@@@@@@@@@                                    '
 echo '                                  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@.                                   '
 echo '                                 @@@@@@@@@@@@@@@@@@@@@@@@@@    @                                   '
 echo '                                 @@@@@@@@@=                  % @-                                  '
 echo '                                @@@@@@@@@@                   =  @                                  '
 echo '                                @@@@@@@@@@                 +    @                                  '
 echo '                                @@@@@@@@@@.    *-@@    @ .@ # + @                                  '
 echo '                                @@@@@@@@@@@    %@ .      @  @ @ @                                  '
 echo '                                @@@@@@@@@@@= @ @*   *         @ @                                  '
 echo '                                @@@@@@@@@@@@                  @@                                   '
 echo '                                 @@@@@@@@@@@@                :@                                    '
 echo '                                  @@@@@@@@@@@@@:        .@@@@@@                                    '
 echo '                                 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@                                   '
 echo '                                 @@@@@@@@%:::::@@  @@@@@@+     @*                                  '
 echo '                                -@@@@@@@         @@@@@@@@ :     @                                  '
 echo '                                .@@@@@@@  #      @@@@@@@@ @     @.                                 '
 echo '                                                                                                   '
 echo '                                                                                                   '



echo -e "${LIGHT_BLUE}Welcome to Roq project setup${NC}"
echo -e "${LIGHT_BLUE}You can always leave an answer empty, default values will be used.${NC}"

# Customize gav
echo ""
read -p "Do you want to adapt your maven's artifact and group id ? (y/N): " use_custom_values

if [ "$use_custom_values" = "y" ]; then
  read -p "Enter group_id: " group_id
  read -p "Enter artifact_id: " artifact_id
fi

# Customize blog description and content
echo ""
read -p "Do you want to provide blog settings or use default ? (y/N): " use_custom_values

if [ "$use_custom_values" = "y" ]; then
  read -p "Enter blog name: " blog_name
  read -p "Enter blog title: " blog_title
  read -p "Enter blog desciption: " blog_description
  read -p "Enter blog welcome message: " blog_welcome_msg
  read -p "Enter facebook handle: " facebook_handle
  read -p "Enter twitter handle: " twitter_handle
  read -p "Enter github handle: " github_handle
  read -p "Enter linkedin handle: " linkedin_handle
fi

# Customize author description
echo ""
read -p "Do you want to provide author description or use default ? (y/N): " use_custom_values

if [ "$use_custom_values" = "y" ]; then
  read -p "Enter author key: " author_key
  read -p "Enter author name: " author_name
  read -p "Enter author job: " author_job
  read -p "Enter author avatar: " author_avatar
  read -p "Enter author nickname: " author_nickname
  read -p "Enter author profile: " author_profile
fi


# Update pom.xml
replace_in_file "$group_id" "<groupId>org.acme</groupId>|<groupId>$group_id</groupId>" pom.xml
replace_in_file "$artifact_id" "<artifactId>roq-with-blog</artifactId>|<artifactId>$artifact_id</artifactId>" pom.xml

# Update index.html
replace_in_file "$blog_name" "name:.*|name: $blog_name" content/index.html
replace_in_file "$blog_title" "title:.*|title: $blog_title" content/index.html
replace_in_file "$blog_description" "description:.*|description: $blog_description" content/index.html
replace_in_file "$facebook_handle" "social-facebook:.*|social-facebook: $facebook_handle" content/index.html
replace_in_file "$twitter_handle" "social-twitter:.*|social-twitter: $twitter_handle" content/index.html
replace_in_file "$github_handle" "social-github:.*|social-github: $github_handle" content/index.html
replace_in_file "$github_handle" "social-linkedin:.*|social-linkedin: $linkedin_handle" content/index.html
replace_in_file "$blog_welcome_msg" "<h1>.*</h1>|<h1>$blog_welcome_msg</h1>" content/index.html


# Update authors.yaml
replace_in_file "$author_key" "roqqy:|$author_key:" data/authors.yml
replace_in_file "$author_name" "name:.*|name: $author_name" data/authors.yml
replace_in_file "$author_job" "job:.*|job: $author_job" data/authors.yml
replace_in_file "$author_nickname" "nickname:.*|nickname: $author_nickname" data/authors.yml
replace_in_file "$author_profile" "profile:.*|profile: $author_profile" data/authors.yml
replace_in_file "$author_avatar" "avatar:.*|avatar: $author_avatar" data/authors.yml

echo ""
echo "Updates completed."