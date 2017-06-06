# sdx-collect

[![Build Status](https://travis-ci.org/ONSdigital/sdx-collect.svg?branch=python-consumer)](https://travis-ci.org/ONSdigital/sdx-collect) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/858afbc345f64b288b8aef4c6600f82d)](https://www.codacy.com/app/ons-sdc/sdx-collect?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ONSdigital/sdx-collect&amp;utm_campaign=Badge_Grade)

``sdx-collect`` is a component of the Office for National Statistics (ONS) Survey Data Exchange (SDX) project which listens to a queue for survey data from the eQ Survey Runner.

On receipt it hands off to sdx-decrypt to decrypt the survey data, validates the result with sdx-validate and then stores the json by calling sdx-store. Once complete the survey is receipted
via the appropriate receipting service.

## Configuration

The following envioronment variables can be set:

| Environment variable      | Default                               | Description
|---------------------------|---------------------------------------|---------------
| SDX_DECRYPT_URL           | ``http://sdx-decrypt:5000/decrypt``   | URL of the ``sdx-decrypt`` service
| SDX_VALIDATE_URL          | ``http://sdx-validate:5000/validate`` | URL of the ``sdx-validate`` service
| SDX_RESPONSES_URL         | ``http://sdx-store:5000/responses``   | URL of the ``sdx-store`` service
| SDX_COLLECT_SECRET        | _none_                                | Key for decrypting messages from queue, must be the same as used for ``sdx-receipt``
| RABBIT_SURVEY_QUEUE       | ``survey``                            | Name of incoming queue
| RABBIT_QUARANTINE_QUEUE   | ``survey_quarantine``                 | Name of queue to quarantine bad decrypt messages to
| RABBIT_EXCHANGE           | ``message``                           | Exchange for incoming queue
| RABBIT_RRM_RECEIPT_QUEUE  | ``rrm_receipt``                       | Name of rrm receipt service queue
| RABBIT_CTP_RECEIPT_QUEUE  | ``ctp_receipt``                       | Name of ctp receipt service queue

### License

Copyright © 2016, Office for National Statistics (https://www.ons.gov.uk)

Released under MIT license, see [LICENSE](LICENSE) for details.
