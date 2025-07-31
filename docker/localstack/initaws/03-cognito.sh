#!/usr/bin/env bash

echo '                                         '
echo '   ____                  _ _             '
echo '  / ___|___   __ _ _ __ (_) |_ ___       '
echo ' | |   / _ \ / _` | '\''_ \| | __/ _ \      '
echo ' | |__| (_) | (_| | | | | | || (_) |     '
echo '  \____\___/ \__, |_| |_|_|\__\___/      '
echo '             |___/                       '
echo '                                         '

awslocal cognito-idp create-user-pool --pool-name veiculo